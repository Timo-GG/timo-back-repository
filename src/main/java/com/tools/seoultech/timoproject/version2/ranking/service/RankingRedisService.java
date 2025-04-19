package com.tools.seoultech.timoproject.version2.ranking.service;

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
    
    private final RedisTemplate<String, Object> redisTemplate;
    
    // Redis에 랭킹 정보 저장
    public void saveRankInfo(Long memberId, Redis_RankingInfo rankingInfo) {
        try {
            int score = rankingInfo.getScore();
            
            // Sorted Set에 점수 저장 (memberId를 키로)
            redisTemplate.opsForZSet().add(RANKING_KEY, memberId.toString(), score);
            
            // Hash에 객체 저장 (memberId를 키로)
            redisTemplate.opsForHash().put(RANKING_OBJECT_KEY, memberId.toString(), rankingInfo);
            
            log.info("Redis에 랭킹 정보 저장 완료: memberId={}, score={}", memberId, score);
        } catch (Exception e) {
            log.error("Redis 랭킹 정보 저장 중 오류 발생: {}", e.getMessage(), e);
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
    
    // 특정 회원의 랭킹 조회
    public Long getRankingPosition(Long memberId) {
        try {
            return redisTemplate.opsForZSet().reverseRank(RANKING_KEY, memberId.toString());
        } catch (Exception e) {
            log.error("회원 랭킹 조회 중 오류 발생: {}", e.getMessage(), e);
            return null;
        }
    }
}