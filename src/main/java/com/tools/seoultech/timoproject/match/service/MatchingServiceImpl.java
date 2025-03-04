package com.tools.seoultech.timoproject.match.service;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.domain.ChatRoomMember;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomMemberRepository;
import com.tools.seoultech.timoproject.chat.repository.ChatRoomRepository;
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
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
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
    public void addToMatchingQueue(Long memberId, MatchingOptionRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

        UserInfo userInfo = createUserInfo(request);
        DuoInfo duoInfo = createDuoInfo(request);
        member.updateMatchOption(userInfo, duoInfo);

        String queueKey = MATCHING_QUEUE_PREFIX + userInfo.getGameMode();
        String userKey = USER_INFO_PREFIX + memberId;

        // 필수 조건을 Redis Hash에 저장 (필터링 시 활용)
        saveMemberToRedis(userKey, userInfo, duoInfo);

        // 대기 시간 초기값 저장 (등록 시간)
        long currentTime = System.currentTimeMillis();
        hashOps.put(userKey, "waitTime", String.valueOf(currentTime));

        // ZSET에 등록 (점수 = 초기 대기 시간 값)
        zSetOps.add(queueKey, memberId.toString(), 0);
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
            removeFromQueue(memberId, String.valueOf(userInfo.getGameMode()));
            removeFromQueue(bestMatch.getId(), String.valueOf(userInfo.getGameMode()));
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

    @Transactional
    protected boolean createChatRoom(String matchId, String matchKey) {
        // 두 명의 사용자 ID 가져오기
        Set<String> memberIds = hashOps.keys(matchKey);
        if (memberIds.size() != 2) {
            log.error("매칭 정보 오류: {}", matchId);
            return false;
        }

        Iterator<String> iterator = memberIds.iterator();
        Long member1 = Long.valueOf(iterator.next());
        Long member2 = Long.valueOf(iterator.next());

        Member user1 = memberRepository.findById(member1)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원: " + member1));
        Member user2 = memberRepository.findById(member2)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원: " + member2));

        // 채팅방 이름은 "{member1Id}_{member2Id}" 형식으로 설정
        String chatRoomName = member1 + "_" + member2;
        ChatRoom chatRoom = ChatRoom.createRoom(chatRoomName);
        chatRoomRepository.save(chatRoom);

        // 채팅방 멤버 추가
        ChatRoomMember chatRoomMember1 = ChatRoomMember.createChatRoomMember(chatRoom, user1);
        ChatRoomMember chatRoomMember2 = ChatRoomMember.createChatRoomMember(chatRoom, user2);
        chatRoomMemberRepository.save(chatRoomMember1);
        chatRoomMemberRepository.save(chatRoomMember2);

        log.info("✅ 채팅방 생성됨: {}", chatRoomName);

        // Redis에서 매칭 정보 삭제
        redisTemplate.delete(matchKey);
        return true;
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

    @Override
    @Transactional
    public boolean acceptMatch(String matchId, Long memberId) {
        String matchKey = MATCH_WAITING_PREFIX + matchId;

        if (!redisTemplate.hasKey(matchKey)) {
            log.warn("매칭이 만료됨: matchId={}", matchId);
            return false; // 매칭이 만료됨
        }

        hashOps.put(matchKey, memberId.toString(), "accepted");

        // 두 명 모두 수락했는지 확인
        Set<String> statusValues = new HashSet<>(hashOps.values(matchKey));
        if (statusValues.contains("pending")) {
            return false; // 아직 한 명이 응답하지 않음
        }

        log.info("매칭 확정: matchId={}", matchId);
        return createChatRoom(matchId, matchKey);
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
    public void removeFromQueue(Long memberId, String gameMode) {
        String queueKey = MATCHING_QUEUE_PREFIX + gameMode;
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
}