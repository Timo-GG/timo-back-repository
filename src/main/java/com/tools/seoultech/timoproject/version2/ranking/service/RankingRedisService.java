package com.tools.seoultech.timoproject.version2.ranking.service;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;
import com.tools.seoultech.timoproject.version2.memberAccount.MemberAccountRepository;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
import com.tools.seoultech.timoproject.version2.ranking.dto.RankingUpdateRequestDto;
import com.tools.seoultech.timoproject.version2.ranking.dto.Redis_RankingInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingRedisService {
    private static final String RANKING_KEY = "lol:ranking";
    private static final String RANKING_OBJECT_KEY = "lol:ranking:objects";
    private final MemberAccountRepository memberAccountRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    public void createInitialRanking(Long memberId, RiotRankingDto riotRankingDto) {
        MemberAccount account = memberAccountRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (account.getRiotAccount() == null || account.getCertifiedUnivInfo() == null) {
            throw new BusinessException(ErrorCode.INVALID_RANKING_INFO);
        }

        Redis_RankingInfo rankingInfo = Redis_RankingInfo.from(memberId, account, riotRankingDto);
        saveRankInfo(memberId, rankingInfo);
        log.info("[Redis 랭킹 등록 완료] memberId={}, score={}", memberId, rankingInfo.getScore());
    }

    public void saveRankInfo(Long memberId, Redis_RankingInfo rankingInfo) {
        try {
            int score = rankingInfo.getScore();

            redisTemplate.opsForZSet().add(RANKING_KEY, memberId.toString(), score);
            redisTemplate.opsForHash().put(RANKING_OBJECT_KEY, memberId.toString(), rankingInfo);

            String universityKey = "lol:ranking:univ:" + rankingInfo.getUniversity().trim();
            redisTemplate.opsForZSet().add(universityKey, memberId.toString(), score);

        } catch (Exception e) {
            log.error("Redis 랭킹 저장 중 오류: {}", e.getMessage(), e);
        }
    }

    public void updateRankingInfo(Long memberId, RankingUpdateRequestDto dto) {
        try {
            Redis_RankingInfo existing = (Redis_RankingInfo) redisTemplate.opsForHash()
                    .get(RANKING_OBJECT_KEY, memberId.toString());

            if (existing == null) {
                log.warn("Redis 랭킹 정보가 존재하지 않아 업데이트 불가: memberId={}", memberId);
                return;
            }

            existing.updateRankingInfo(dto);

            redisTemplate.opsForHash().put(RANKING_OBJECT_KEY, memberId.toString(), existing);
            log.info("Redis 랭킹 정보 업데이트 완료: memberId={}", memberId);
        } catch (Exception e) {
            log.error("Redis 랭킹 업데이트 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    public List<Redis_RankingInfo> getTopRankingsByUniversity(String university, int limit) {
        try {
            String universityKey = "lol:ranking:univ:" + university.trim();
            Set<Object> topMembers = redisTemplate.opsForZSet().reverseRange(universityKey, 0, limit - 1);

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
            log.error("대학교별 랭킹 조회 중 오류 발생: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // 상위 랭킹 정보 조회
    public List<Redis_RankingInfo> getTopRankings(int limit) {
        try {
            // 점수 기준 상위 랭킹 조회
            Set<Object> topMembers = redisTemplate.opsForZSet().reverseRange(RANKING_KEY, 0, limit - 1);

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
            log.error("상위 랭킹 정보 조회 중 오류 발생: {}", e.getMessage(), e);
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
            log.error("내 랭킹 정보 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.REDIS_ERROR);
        }
    }

    public void flushAllRankingData() {
        redisTemplate.delete(RANKING_KEY); // 전체 키 삭제
        redisTemplate.delete(RANKING_OBJECT_KEY); // 객체 저장소 삭제
    }

    public void deleteRankingByMemberId(Long memberId) {
        String id = memberId.toString();

        // 1) 해시에서 정보 꺼내서 대학교별 ZSET 삭제할 키 알아내기
        Object raw = redisTemplate.opsForHash().get(RANKING_OBJECT_KEY, id);
        if (raw instanceof Redis_RankingInfo) {
            Redis_RankingInfo info = (Redis_RankingInfo) raw;
            String univKey = "lol:ranking:univ:" + info.getUniversity().trim();
            redisTemplate.opsForZSet().remove(univKey, id);
        }

        // 2) 전체 랭킹 ZSET 에서 제거
        redisTemplate.opsForZSet().remove(RANKING_KEY, id);
        // 3) 해시(객체 저장소) 에서 제거
        redisTemplate.opsForHash().delete(RANKING_OBJECT_KEY, id);

        log.info("[Redis 랭킹 삭제 완료] memberId={}", memberId);
    }

}