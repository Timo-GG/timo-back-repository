package com.tools.seoultech.timoproject.match.service;

import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.match.domain.DuoInfo;
import com.tools.seoultech.timoproject.match.domain.UserInfo;
import com.tools.seoultech.timoproject.match.dto.MatchingOptionRequest;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

    private final MemberRepository memberRepository;
    private final ChatService chatService;
    private final StringRedisTemplate redisTemplate;
    private final ZSetOperations<String, String> zSetOps;   // 대기열 관리
    private HashOperations<String, String, String> hashOps; // 회원 정보 저장

    private static final String MATCHING_QUEUE_PREFIX = "matching:";
    private static final String USER_INFO_PREFIX = "user:";
    private static final double WAIT_TIME_WEIGHT = 0.01; // 대기 시간 가중치 (초당 0.01점 증가)
    private static final String MATCH_WAITING_PREFIX = "match:waiting:";
    private static final long MATCH_ACCEPT_TIMEOUT = 60; // 60초 내 응답 없으면 매칭 취소

    /** HashOperations 초기화 */
    @PostConstruct
    private void init() {
        this.hashOps = redisTemplate.opsForHash();
    }

    @Override
    public List<Long> getWaitingUsers(String gameMode) {
        String queueKey = MATCHING_QUEUE_PREFIX + gameMode;

        // 대기열에서 유저 ID 조회
        Set<String> waitingUserIds = zSetOps.range(queueKey, 0, -1);

        // 유저 ID 리스트를 Long 타입으로 변환하여 반환
        if (waitingUserIds != null) {
            return waitingUserIds.stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList(); // 대기열이 비어 있을 경우 빈 리스트 반환
        }
    }

    @Override
    public Optional<String> startMatch(Long memberId, MatchingOptionRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

        UserInfo userInfo = createUserInfo(request);
        DuoInfo duoInfo = createDuoInfo(request);

        member.updateMatchOption(userInfo, duoInfo);
        memberRepository.flush();

        String queueKey = MATCHING_QUEUE_PREFIX + userInfo.getGameMode();
        String userKey = USER_INFO_PREFIX + memberId;

        // 1. 먼저 대기열을 조회하여 바로 매칭 가능한지 확인
        Optional<String> matchId = findMatch(memberId);
        if (matchId.isPresent()) {
            return matchId; // 즉시 매칭이 가능하면 Redis에 등록하지 않고 바로 반환
        }

        // 2. 매칭이 불가능한 경우에만 Redis에 등록
        saveMemberToRedis(userKey, userInfo, duoInfo);
        long currentTime = System.currentTimeMillis();
        hashOps.put(userKey, "waitTime", String.valueOf(currentTime));

        zSetOps.add(queueKey, memberId.toString(), 0);
        log.info("대기열에 추가됨: {}", memberId);

        // 3. 대기 상태 유지
        return Optional.empty();
    }

    @Override
    public Optional<String> findMatch(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

        UserInfo userInfo = member.getUserInfo();
        String queueKey = MATCHING_QUEUE_PREFIX + userInfo.getGameMode();

        // 필수 조건을 만족하는 후보자만 필터링
        Set<String> candidateIds = filterCandidates(queueKey, userInfo);
        if (candidateIds.isEmpty()) {
            return Optional.empty();
        }

        Member bestMatch = null;
        double bestScore = Double.MAX_VALUE; // 점수가 낮을수록 우선순위

        for (String candidateIdStr : candidateIds) {
            Long candidateId = Long.valueOf(candidateIdStr);

            if (candidateId.equals(memberId)) {
                continue;
            }

            Member candidate = memberRepository.findById(candidateId)
                    .orElseThrow(() -> new IllegalArgumentException("Candidate not found: " + candidateId));

            double score = calculateMatchingScore(userInfo, member.getDuoInfo(),
                    candidate.getUserInfo(), candidate.getDuoInfo(), candidateId);

            if (score < bestScore) {
                bestScore = score;
                bestMatch = candidate;
            }
        }

        if (bestMatch != null) {
            removeFromQueue(memberId);
            removeFromQueue(bestMatch.getId());
            return createMatchRequest(memberId, bestMatch.getId());
        }

        return Optional.empty();
    }

    private Optional<String> createMatchRequest(Long member1, Long member2) {
        String matchId = UUID.randomUUID().toString();
        String matchKey = MATCH_WAITING_PREFIX + matchId;

        hashOps.put(matchKey, member1.toString(), "pending");
        hashOps.put(matchKey, member2.toString(), "pending");

        // 60초 내 응답 없으면 자동 삭제
        redisTemplate.expire(matchKey, MATCH_ACCEPT_TIMEOUT, TimeUnit.SECONDS);

        log.info("매칭 요청 생성: {} vs {}", member1, member2);
        return Optional.of(matchId);
    }

    private UserInfo createUserInfo(MatchingOptionRequest request) {
        return UserInfo.builder()
                .introduce(request.getUserInfo().getIntroduce())
                .gameMode(request.getUserInfo().getGameMode())
                .playPosition(request.getUserInfo().getPlayPosition())
                .playCondition(request.getUserInfo().getPlayCondition())
                .voiceChat(request.getUserInfo().getVoiceChat())
                .playStyle(request.getUserInfo().getPlayStyle())
                .build();
    }
    private DuoInfo createDuoInfo(MatchingOptionRequest request) {
        return DuoInfo.builder()
                .duoPlayPosition(request.getDuoInfo().getDuoPlayPosition())
                .duoPlayStyle(request.getDuoInfo().getDuoPlayStyle())
                .build();
    }

    /** 필수 조건 필터링 */
    private Set<String> filterCandidates(String queueKey, UserInfo userInfo) {
        Set<String> allCandidates = zSetOps.range(queueKey, 0, -1);

        return allCandidates != null ? allCandidates.stream()
                .filter(candidateId -> isMatchingOptionValid(candidateId, userInfo))
                .collect(Collectors.toSet()) : Collections.emptySet();
    }
    private boolean isMatchingOptionValid(String candidateId, UserInfo userInfo) {

        String userKey = USER_INFO_PREFIX + candidateId;

        List<String> userFields = hashOps.multiGet(userKey, List.of("gameMode", "voiceChat"));
        if (userFields.size() != 2 || userFields.contains(null)) {
            return false;
        }

        String storedGameMode = userFields.get(0);
        String storedVoiceChat = userFields.get(1);

        String myGameMode = String.valueOf(userInfo.getGameMode());
        String myVoiceChat = String.valueOf(userInfo.getVoiceChat());

        // 필수 조건이 맞지 않으면 제외
        return Objects.equals(storedGameMode, myGameMode) && Objects.equals(storedVoiceChat, myVoiceChat);
    }

    /** 부가 조건 점수 계산 */
    private double calculateMatchingScore(UserInfo userInfo, DuoInfo duoInfo,
                                          UserInfo candidateUserInfo, DuoInfo candidateDuoInfo, Long candidateId) {
        double score = 0;

        // 대기 시간 가중치 추가
        long candidateWaitTime = getWaitTime(candidateId);
        score -= candidateWaitTime * WAIT_TIME_WEIGHT; // 오래 기다릴수록 점수 낮아짐 (좋은 매칭)

        // 포지션 조합 (서로 다르면 +20점, 같으면 -10점)
        if (!duoInfo.getDuoPlayPosition().equals(candidateUserInfo.getPlayPosition())) {
            score += 20;
        } else {
            score -= 10;
        }

        // 플레이 스타일 가산점
        if (duoInfo.getDuoPlayStyle().equals(candidateUserInfo.getPlayStyle())) {
            score -= 15;
        } else {
            score += 15;
        }

        // 플레이 조건 (캐주얼 vs 경쟁적)
        if (userInfo.getPlayCondition().equals(candidateUserInfo.getPlayCondition())) {
            score -= 10;
        } else {
            score += 10;
        }

        return score;
    }

    /** 대기 시간 계산 */
    private long getWaitTime(Long memberId) {
        String waitTimeStr = hashOps.get(USER_INFO_PREFIX + memberId, "waitTime");
        if (waitTimeStr == null) return 0;
        long waitTime = Long.parseLong(waitTimeStr);
        return (System.currentTimeMillis() - waitTime) / 1000; // 초 단위 변환
    }

    /** Redis 대기열에 등록 */
    private void saveMemberToRedis(String userKey, UserInfo userInfo, DuoInfo duoInfo) {
        if (userInfo == null || duoInfo == null) {
            throw new IllegalArgumentException("UserInfo or DuoInfo cannot be null");
        }

        Map<String, String> userMap = Map.of(
                "gameMode", String.valueOf(userInfo.getGameMode()),
                "voiceChat", String.valueOf(userInfo.getVoiceChat()),
                "playPosition", String.valueOf(userInfo.getPlayPosition()),
                "playStyle", String.valueOf(userInfo.getPlayStyle()),
                "waitTime", String.valueOf(System.currentTimeMillis())
        );
        hashOps.putAll(userKey, userMap);
    }

    @Transactional
    @Override
    public boolean acceptMatch(String matchId, Long memberId) {
        String matchKey = MATCH_WAITING_PREFIX + matchId;

        if (!redisTemplate.hasKey(matchKey)) {
            log.warn("매칭이 만료됨: matchId={}", matchId);
            return false;
        }

        hashOps.put(matchKey, memberId.toString(), "accepted");

        // 두 명 모두 수락했는지 확인
        Set<String> statusValues = new HashSet<>(hashOps.values(matchKey));
        if (statusValues.contains("pending")) {
            return false; // 아직 다른 사용자의 응답 대기
        }

        // 여기서 매칭이 최종 확정된 상태
        log.info("매칭 확정: matchId={}", matchId);

        // 두 사용자 정보를 얻어와서 ChatService를 통해 채팅방 생성
        return finalizeMatch(matchId, matchKey);
    }

    @Transactional
    protected boolean finalizeMatch(String matchId, String matchKey) {
        // 두 명의 사용자 ID 가져오기
        Set<String> memberIds = hashOps.keys(matchKey);
        if (memberIds == null || memberIds.size() != 2) {
            log.error("매칭 정보 오류: matchId={}", matchId);
            return false;
        }

        Iterator<String> iterator = memberIds.iterator();
        Long member1 = Long.valueOf(iterator.next());
        Long member2 = Long.valueOf(iterator.next());

        // ChatService를 통해 채팅방 생성 & 두 멤버 가입
        chatService.createChatRoomForMatch(matchId, member1, member2);

        // Redis에서 매칭 정보 삭제
        redisTemplate.delete(matchKey);
        return true;
    }

    @Override
    @Transactional
    public boolean denyMatch(String matchId, Long memberId) {
        String matchKey = MATCH_WAITING_PREFIX + matchId;

        if (!redisTemplate.hasKey(matchKey)) {
            log.warn("매칭이 이미 만료됨: matchId={}", matchId);
            return false; // 매칭이 이미 취소된 상태
        }

        // 상대방 ID 가져오기
        Set<String> memberIds = hashOps.keys(matchKey);
        if (memberIds == null || memberIds.size() != 2) {
            log.error("매칭 정보 오류: {}", matchId);
            return false;
        }

        Iterator<String> iterator = memberIds.iterator();
        Long member1 = Long.valueOf(iterator.next());
        Long member2 = Long.valueOf(iterator.next());

        Long otherMemberId = member1.equals(memberId) ? member2 : member1;

        // 매칭 요청 삭제 (매칭 수락 상태 초기화)
        redisTemplate.delete(matchKey);

        // 거절 당한 상대방은 다시 대기열에 남아 있도록 함
        requeueMember(otherMemberId);

        log.info("매칭 거절됨: {} vs {} (거절한 사람: {})", member1, member2, memberId);
        return true;
    }

    /** 매칭 대기열에서 제거 */
    public void removeFromQueue(Long memberId) {
        String userKey = USER_INFO_PREFIX + memberId;
        String queueKey = MATCHING_QUEUE_PREFIX + hashOps.get(userKey, "gameMode");

        zSetOps.remove(queueKey, memberId.toString());
        redisTemplate.delete(USER_INFO_PREFIX + memberId);
    }

    /** 거절당한 상대방 다시 매칭 진행 */
    private void requeueMember(Long memberId) {
        String userKey = USER_INFO_PREFIX + memberId;
        String queueKey = MATCHING_QUEUE_PREFIX + hashOps.get(userKey, "gameMode");

        // 기존 대기 시간을 유지
        String waitTimeStr = hashOps.get(userKey, "waitTime");
        long waitTime = waitTimeStr != null ? Long.parseLong(waitTimeStr) : System.currentTimeMillis();

        // 다시 대기열에 추가 (기존 대기 시간 유지)
        zSetOps.add(queueKey, memberId.toString(), 0);
        hashOps.put(userKey, "waitTime", String.valueOf(waitTime));

        log.info("거절 후 재등록: memberId={} (기존 대기 시간 유지)", memberId);
    }

    // MatchingServiceImpl.java (일부 발췌)
    @Override
    public Set<Long> getMatchMemberIds(String matchId) {
        String matchKey = MATCH_WAITING_PREFIX + matchId;
        // hashOps.keys(matchKey)는 Redis에 저장된 key들(회원 ID 문자열)을 반환합니다.
        Set<String> memberIdStrSet = hashOps.keys(matchKey);
        if (memberIdStrSet == null) {
            return null;
        }
        // 문자열을 Long으로 변환하여 반환
        Set<Long> memberIds = new HashSet<>();
        for (String idStr : memberIdStrSet) {
            memberIds.add(Long.valueOf(idStr));
        }
        return memberIds;
    }
}