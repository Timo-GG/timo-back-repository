package com.tools.seoultech.timoproject.ranking.service;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;
import com.tools.seoultech.timoproject.memberAccount.MemberAccountRepository;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
import com.tools.seoultech.timoproject.ranking.RankingInfo;
import com.tools.seoultech.timoproject.ranking.dto.RankingUpdateRequestDto;
import com.tools.seoultech.timoproject.ranking.dto.Redis_RankingInfo;
import com.tools.seoultech.timoproject.socket.controller.RankingSocketController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingRedisService {
    private static final String RANKING_KEY = "lol:ranking";
    private static final String RANKING_OBJECT_KEY = "lol:ranking:objects";

    private final MemberAccountRepository memberAccountRepository;
    private final RankingInfoRepository rankingInfoRepository;

    @Qualifier("rankingRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;

    public void createInitialRanking(Long memberId, RiotRankingDto riotRankingDto) {
        MemberAccount account = memberAccountRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (account.getRiotAccount() == null || account.getCertifiedUnivInfo() == null) {
            throw new BusinessException(ErrorCode.INVALID_RANKING_INFO);
        }

        Redis_RankingInfo rankingInfo = Redis_RankingInfo.from(memberId, account, riotRankingDto);
        rankingInfoRepository.findByMemberAccountMemberId(memberId)
                .ifPresent(existing -> {
                    String department = account.getCertifiedUnivInfo().getDepartment();
                    RankingUpdateRequestDto updateRequestDto = RankingUpdateRequestDto.fromEntity(existing, department);
                    rankingInfo.updateRankingInfo(updateRequestDto);
                });

        saveRankInfo(memberId, rankingInfo);
        log.info("[Redis 등록 완료] memberId={}, score={}", memberId, rankingInfo.getScore());
    }

    public void saveRankInfo(Long memberId, Redis_RankingInfo rankingInfo) {
        try {
            int score = rankingInfo.getScore();

            redisTemplate.opsForZSet().add(RANKING_KEY, memberId.toString(), score);
            redisTemplate.opsForHash().put(RANKING_OBJECT_KEY, memberId.toString(), rankingInfo);

            String universityKey = buildUniversityKey(rankingInfo.getUniversity());
            redisTemplate.opsForZSet().add(universityKey, memberId.toString(), score);

            log.info("랭킹 저장 완료: memberId={}, university={}", memberId, rankingInfo.getUniversity());
        } catch (Exception e) {
            log.error("Redis 랭킹 저장 중 오류: {}", e.getMessage(), e);
        }
    }

    public void updateRankingInfo(Long memberId, RankingUpdateRequestDto dto) {
        try {
            Redis_RankingInfo existing = (Redis_RankingInfo) redisTemplate.opsForHash()
                    .get(RANKING_OBJECT_KEY, memberId.toString());

            if (existing == null) {
                log.warn("업데이트 실패: Redis 랭킹 정보 없음, memberId={}", memberId);
                return;
            }

            existing.updateRankingInfo(dto);
            redisTemplate.opsForHash().put(RANKING_OBJECT_KEY, memberId.toString(), existing);

            log.info("유저 정보 업데이트 완료: memberId={}", memberId);
        } catch (Exception e) {
            log.error("Redis 랭킹 업데이트 중 오류: {}", e.getMessage(), e);
        }
    }

    public List<Redis_RankingInfo> getTopRankingsByUniversity(String university, int limit) {
        return getTopRankings(buildUniversityKey(university), limit);
    }

    public List<Redis_RankingInfo> getTopRankings(int limit) {
        return getTopRankings(RANKING_KEY, limit);
    }

    private List<Redis_RankingInfo> getTopRankings(String key, int limit) {
        try {
            Set<Object> topMembers = redisTemplate.opsForZSet().reverseRange(key, 0, limit - 1);

            if (topMembers == null || topMembers.isEmpty()) {
                return new ArrayList<>();
            }

            List<Redis_RankingInfo> result = new ArrayList<>();
            for (Object memberId : topMembers) {
                Redis_RankingInfo rankInfo = (Redis_RankingInfo) redisTemplate.opsForHash()
                        .get(RANKING_OBJECT_KEY, memberId.toString());

                if (rankInfo != null) {
                    result.add(rankInfo);
                }
            }

            return result;
        } catch (Exception e) {
            log.error("랭킹 조회 중 오류: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public Redis_RankingInfo getMyRankingInfo(Long memberId) {
        try {
            Redis_RankingInfo rankInfo = (Redis_RankingInfo) redisTemplate.opsForHash()
                    .get(RANKING_OBJECT_KEY, memberId.toString());

            if (rankInfo == null) {
                throw new BusinessException(ErrorCode.REDIS_RANKING_NOT_FOUND);
            }

            return rankInfo;
        } catch (Exception e) {
            log.error("내 랭킹 조회 중 오류: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.REDIS_ERROR);
        }
    }

    public void flushAllRankingData() {
        redisTemplate.delete(RANKING_KEY);
        redisTemplate.delete(RANKING_OBJECT_KEY);
        log.info("모든 랭킹 데이터 플러시 완료");
    }

    public void deleteRankingByMemberId(Long memberId) {
        String id = memberId.toString();

        Object raw = redisTemplate.opsForHash().get(RANKING_OBJECT_KEY, id);
        if (raw instanceof Redis_RankingInfo info) {
            String univKey = buildUniversityKey(info.getUniversity());
            redisTemplate.opsForZSet().remove(univKey, id);
        }

        redisTemplate.opsForZSet().remove(RANKING_KEY, id);
        redisTemplate.opsForHash().delete(RANKING_OBJECT_KEY, id);

        log.info("랭킹 삭제 완료: memberId={}", memberId);
    }

    private String buildUniversityKey(String university) {
        try {
            return "lol:ranking:univ:" + URLEncoder.encode(university.trim(), StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            log.warn("대학교 키 인코딩 실패, 원본 사용: {}", university, e);
            return "lol:ranking:univ:" + university.trim();
        }
    }
}
