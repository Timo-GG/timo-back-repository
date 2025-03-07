package com.tools.seoultech.timoproject.match.service;

import com.tools.seoultech.timoproject.chat.domain.ChatRoom;
import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.match.domain.DuoInfo;
import com.tools.seoultech.timoproject.match.domain.UserInfo;
import com.tools.seoultech.timoproject.match.dto.FinalizedMatchResult;
import com.tools.seoultech.timoproject.match.dto.MatchResponseStatus;
import com.tools.seoultech.timoproject.match.dto.MatchResult;
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
    private static final long MATCH_ACCEPT_TIMEOUT = 60 * 30; // 60초 내 응답 없으면 매칭 취소

    @PostConstruct
    private void init() {
        this.hashOps = redisTemplate.opsForHash();
    }

    @Override
    public List<Long> getWaitingUsers(String gameMode) {
        String queueKey = MATCHING_QUEUE_PREFIX + gameMode;
        Set<String> waitingUserIds = zSetOps.range(queueKey, 0, -1);
        if (waitingUserIds != null) {
            return waitingUserIds.stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<String> startMatch(Long memberId, MatchingOptionRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found : " + memberId));

        UserInfo userInfo = request.toUserInfo();
        DuoInfo duoInfo = request.toDuoInfo();

        member.updateMatchOption(userInfo, duoInfo);
        memberRepository.flush();

        String queueKey = MATCHING_QUEUE_PREFIX + userInfo.getGameMode();
        String userKey = USER_INFO_PREFIX + memberId;

        // 즉시 매칭 가능한지 확인
        Optional<String> matchId = findMatch(memberId);
        if (matchId.isPresent()) {
            return matchId;
        }

        // 매칭 불가능하면 Redis에 등록
        saveMemberToRedis(userKey, userInfo, duoInfo);
        long currentTime = System.currentTimeMillis();
        hashOps.put(userKey, "waitTime", String.valueOf(currentTime));
        zSetOps.add(queueKey, memberId.toString(), 0);
        log.info("대기열에 추가됨: {}", memberId);
        return Optional.empty();
    }

    @Override
    public void saveTestDataToRedis() {
        for (int i = 5; i <= 8; i++) {
            Long memberId = (long) i;
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("Member not found : " + memberId));
            UserInfo userInfo = member.getUserInfo();
            DuoInfo duoInfo = member.getDuoInfo();
            String queueKey = MATCHING_QUEUE_PREFIX + userInfo.getGameMode();
            String userKey = USER_INFO_PREFIX + memberId;
            saveMemberToRedis(userKey, userInfo, duoInfo);
            long currentTime = System.currentTimeMillis();
            hashOps.put(userKey, "waitTime", String.valueOf(currentTime));
            zSetOps.add(queueKey, memberId.toString(), 0);
            log.info("대기열에 추가됨: {}", memberId);
        }
    }

    @Override
    public Optional<String> findMatch(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

        UserInfo userInfo = member.getUserInfo();
        DuoInfo duoInfo = member.getDuoInfo();
        String queueKey = MATCHING_QUEUE_PREFIX + userInfo.getGameMode();

        // 필수 조건을 만족하는 후보자만 필터링
        Set<String> candidateIds = filterCandidates(queueKey, userInfo, duoInfo);
        if (candidateIds.isEmpty()) {
            return Optional.empty();
        }

        Member bestMatch = null;
        double bestScore = Double.MAX_VALUE;
        for (String candidateIdStr : candidateIds) {
            Long candidateId = Long.valueOf(candidateIdStr);
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
        redisTemplate.expire(matchKey, MATCH_ACCEPT_TIMEOUT, TimeUnit.SECONDS);
        log.info("매칭 요청 생성: {} vs {}", member1, member2);
        return Optional.of(matchId);
    }

    private Set<String> filterCandidates(String queueKey, UserInfo userInfo, DuoInfo duoInfo) {
        Set<String> allCandidates = zSetOps.range(queueKey, 0, -1);
        return allCandidates != null ? allCandidates.stream()
                .filter(candidateId -> isMatchingOptionValid(candidateId, userInfo, duoInfo))
                .collect(Collectors.toSet()) : Collections.emptySet();
    }

    private boolean isMatchingOptionValid(String candidateId, UserInfo userInfo, DuoInfo duoInfo) {
        String userKey = USER_INFO_PREFIX + candidateId;
        List<String> userFields = hashOps.multiGet(userKey, List.of("playPosition"));
        if (userFields.contains(null)) {
            return false;
        }
        String storedPlayPosition = userFields.get(0);
        String myDesiredPosition = String.valueOf(duoInfo.getDuoPlayPosition());
        log.info("storedPlayPosition={}, myDesiredPosition={}", storedPlayPosition, myDesiredPosition);
        return Objects.equals(storedPlayPosition, myDesiredPosition);
    }

    private double calculateMatchingScore(UserInfo userInfo, DuoInfo duoInfo,
                                          UserInfo candidateUserInfo, DuoInfo candidateDuoInfo, Long candidateId) {
        double score = 0;
        long candidateWaitTime = getWaitTime(candidateId);
        score -= candidateWaitTime * WAIT_TIME_WEIGHT;
        if (userInfo.getPlayStyle().equals(candidateUserInfo.getPlayStyle())) {
            score -= 15;
        } else {
            score += 15;
        }
        if (userInfo.getPlayPosition().equals(candidateDuoInfo.getDuoPlayPosition())) {
            score -= 15;
        } else {
            score += 15;
        }
        if (userInfo.getVoiceChat().equals(candidateUserInfo.getVoiceChat())) {
            score -= 15;
        } else {
            score += 15;
        }
        return score;
    }

    private long getWaitTime(Long memberId) {
        String waitTimeStr = hashOps.get(USER_INFO_PREFIX + memberId, "waitTime");
        if (waitTimeStr == null) return 0;
        long waitTime = Long.parseLong(waitTimeStr);
        return (System.currentTimeMillis() - waitTime) / 1000;
    }

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
    public MatchResult acceptMatch(String matchId, Long memberId) {
        String matchKey = MATCH_WAITING_PREFIX + matchId;
        if (!redisTemplate.hasKey(matchKey)) {
            log.warn("매칭이 만료됨 또는 취소됨: matchId={}", matchId);
            return new MatchResult(MatchResponseStatus.DECLINED, null, null);
        }
        hashOps.put(matchKey, memberId.toString(), "accepted");
        Set<String> statusValues = new HashSet<>(hashOps.values(matchKey));
        if (statusValues.contains("pending")) {
            return new MatchResult(MatchResponseStatus.PENDING, null, null);
        }
        log.info("매칭 확정: matchId={}", matchId);
        Optional<FinalizedMatchResult> finalizedOpt = finalizeMatch(matchId, matchKey);
        if (finalizedOpt.isPresent()) {
            FinalizedMatchResult finalized = finalizedOpt.get();
            return new MatchResult(MatchResponseStatus.ACCEPTED, finalized.chatRoomId(), finalized.memberIds());
        } else {
            return new MatchResult(MatchResponseStatus.DECLINED, null, null);
        }
    }

    @Transactional
    protected Optional<FinalizedMatchResult> finalizeMatch(String matchId, String matchKey) {
        Set<String> memberIdStrSet = hashOps.keys(matchKey);
        if (memberIdStrSet == null || memberIdStrSet.size() != 2) {
            log.error("매칭 정보 오류: matchId={}", matchId);
            return Optional.empty();
        }
        Set<Long> memberIds = memberIdStrSet.stream()
                .map(Long::valueOf)
                .collect(Collectors.toSet());
        Iterator<Long> iterator = memberIds.iterator();
        Long member1 = iterator.next();
        Long member2 = iterator.next();
        chatService.createChatRoomForMatch(matchId, member1, member2);
        ChatRoom chatRoom = chatService.findChatRoomByMatchId(matchId);
        Long chatRoomId = chatRoom != null ? chatRoom.getId() : null;
        redisTemplate.delete(matchKey);
        return Optional.of(new FinalizedMatchResult(chatRoomId, memberIds));
    }

    @Override
    @Transactional
    public boolean denyMatch(String matchId, Long memberId) {
        String matchKey = MATCH_WAITING_PREFIX + matchId;
        if (!redisTemplate.hasKey(matchKey)) {
            log.warn("매칭이 이미 만료됨: matchId={}", matchId);
            return false;
        }
        Set<String> memberIds = hashOps.keys(matchKey);
        if (memberIds == null || memberIds.size() != 2) {
            log.error("매칭 정보 오류: {}", matchId);
            return false;
        }
        Iterator<String> iterator = memberIds.iterator();
        Long member1 = Long.valueOf(iterator.next());
        Long member2 = Long.valueOf(iterator.next());
        Long otherMemberId = member1.equals(memberId) ? member2 : member1;
        redisTemplate.delete(matchKey);
        requeueMember(otherMemberId);
        log.info("매칭 거절됨: {} vs {} (거절한 사람: {})", member1, member2, memberId);
        return true;
    }

    @Override
    public void removeFromQueue(Long memberId) {
        String userKey = USER_INFO_PREFIX + memberId;
        String queueKey = MATCHING_QUEUE_PREFIX + hashOps.get(userKey, "gameMode");
        zSetOps.remove(queueKey, memberId.toString());
        redisTemplate.delete(USER_INFO_PREFIX + memberId);
    }

    @Override
    public void removeAllFromQueue(String gameMode) {
        String queueKey = MATCHING_QUEUE_PREFIX + gameMode;
        Set<String> allUserIds = zSetOps.range(queueKey, 0, -1);
        if (allUserIds != null && !allUserIds.isEmpty()) {
            for (String userId : allUserIds) {
                zSetOps.remove(queueKey, userId);
            }
            log.info("대기열에서 모든 유저를 삭제했습니다. (게임 모드: {})", gameMode);
        } else {
            log.info("대기열이 비어 있습니다. (게임 모드: {})", gameMode);
        }
    }

    private void requeueMember(Long memberId) {
        String userKey = USER_INFO_PREFIX + memberId;
        String queueKey = MATCHING_QUEUE_PREFIX + hashOps.get(userKey, "gameMode");
        String waitTimeStr = hashOps.get(userKey, "waitTime");
        long waitTime = waitTimeStr != null ? Long.parseLong(waitTimeStr) : System.currentTimeMillis();
        zSetOps.add(queueKey, memberId.toString(), 0);
        hashOps.put(userKey, "waitTime", String.valueOf(waitTime));
        log.info("거절 후 재등록: memberId={} (기존 대기 시간 유지)", memberId);
    }

    @Override
    public Set<Long> getMatchMemberIds(String matchId) {
        String matchKey = MATCH_WAITING_PREFIX + matchId;
        Set<String> memberIdStrSet = hashOps.keys(matchKey);
        if (memberIdStrSet == null) {
            return null;
        }
        Set<Long> memberIds = new HashSet<>();
        for (String idStr : memberIdStrSet) {
            memberIds.add(Long.valueOf(idStr));
        }
        return memberIds;
    }
}
