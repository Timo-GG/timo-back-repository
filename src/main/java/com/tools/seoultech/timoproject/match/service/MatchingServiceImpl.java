package com.tools.seoultech.timoproject.match.service;

import com.tools.seoultech.timoproject.match.domain.DuoInfo;
import com.tools.seoultech.timoproject.match.domain.UserInfo;
import com.tools.seoultech.timoproject.match.dto.MatchingOptionRequest;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService{

    private final MemberRepository memberRepository;
    private final StringRedisTemplate redisTemplate;

    private static final String MATCHING_QUEUE_PREFIX = "matching:";

    @Override
    public void addToMatchingQueue(Long memberId, MatchingOptionRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

        UserInfo userInfo = createUserInfo(request);
        DuoInfo duoInfo = createDuoInfo(request);
        member.updateMatchOption(userInfo, duoInfo);

        // GameMode 별로 대기열 관리
        String queueKey = MATCHING_QUEUE_PREFIX + userInfo.getGameMode();

        // Redis ZSET에 추가 (점수는 우선 0으로 설정, 매칭할 때 계산)
        redisTemplate.opsForZSet().add(queueKey, memberId.toString(), 0);
    }

    @Override
    public Optional<Member> findMatch(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

        UserInfo userInfo = member.getUserInfo();
        DuoInfo duoInfo = member.getDuoInfo();
        String queueKey = MATCHING_QUEUE_PREFIX + userInfo.getGameMode();

        // Redis에서 매칭 대기열 가져오기
        Set<String> candidateIds = redisTemplate.opsForZSet().range(queueKey, 0, -1);
        if (candidateIds == null || candidateIds.isEmpty()) {
            return Optional.empty();
        }

        Member bestMatch = null;
        double bestScore = Double.MAX_VALUE;
        for (String candidateIdStr : candidateIds) {
            Long candidateId = Long.valueOf(candidateIdStr);

            if (candidateId.equals(memberId)) {
                continue;
            }

            Member candidate = memberRepository.findById(candidateId)
                    .orElseThrow(() -> new IllegalArgumentException("Candidate not found: " + candidateId));
            UserInfo candidateUserInfo = candidate.getUserInfo();

            // 필수 필터링: 같은 포지션이면 매칭 X
            if (userInfo.getPlayPosition().equals(candidateUserInfo.getPlayPosition())) {
                continue;
            }

            // 매칭 점수 계산
            double score = calculateMatchingScore(userInfo, duoInfo, candidateUserInfo, candidate.getDuoInfo());

            // Redis에 점수 업데이트 (ZSET의 점수 조정)
            redisTemplate.opsForZSet().incrementScore(queueKey, candidateIdStr, score);

            if (score < bestScore) {
                bestScore = score;
                bestMatch = candidate;
            }
        }

        if (bestMatch != null) {
            removeFromQueue(memberId, String.valueOf(userInfo.getGameMode()));
            removeFromQueue(bestMatch.getId(), String.valueOf(userInfo.getGameMode()));
            return Optional.of(bestMatch);
        }

        return Optional.empty();
    }

    public void removeFromQueue(Long memberId, String gameMode) {
        String queueKey = MATCHING_QUEUE_PREFIX + gameMode;
        redisTemplate.opsForZSet().remove(queueKey, memberId.toString());
    }

    private UserInfo createUserInfo(MatchingOptionRequest request) {
        return new UserInfo(
                request.userInfo().introduce(),
                request.userInfo().gameMode(),
                request.userInfo().playPosition(),
                request.userInfo().playCondition(),
                request.userInfo().voiceChat(),
                request.userInfo().playStyle()
        );
    }

    private DuoInfo createDuoInfo(MatchingOptionRequest request) {
        return new DuoInfo(
                request.duoInfo().duoPlayPosition(),
                request.duoInfo().duoPlayStyle()
        );
    }

    private double calculateMatchingScore(UserInfo userInfo, DuoInfo duoInfo,
                                          UserInfo candidateUserInfo, DuoInfo candidateDuoInfo) {

        double score = 0;

        // TODO : 매칭 우선순위 점수 계산

        if (!duoInfo.getDuoPlayStyle().equals(candidateUserInfo.getPlayStyle())) {
            score += 20;
        }

        if (duoInfo.getDuoPlayPosition().equals(candidateUserInfo.getPlayPosition())) {
            score -= 30;
        }

        if (!userInfo.getPlayCondition().equals(candidateUserInfo.getPlayCondition())) {
            score += 15;
        }

        if (!userInfo.getVoiceChat().equals(candidateUserInfo.getVoiceChat())) {
            score += 10;
        }

        return score;
    }
}

