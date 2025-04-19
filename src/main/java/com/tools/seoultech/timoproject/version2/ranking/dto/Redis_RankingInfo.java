package com.tools.seoultech.timoproject.version2.ranking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
@ToString
public class Redis_RankingInfo implements Serializable {
    private final Long memberId;
    private final String puuid;
    
    // 소환사 정보
    private final String gameName;
    private final String tagLine;
    
    // 대학 정보
    private final String university;
    private final String department;
    
    // 라이엇 API 정보
    private final String tier;
    private final String rank;
    private final int lp;
    private final List<String> mostChampions;
    private final int wins;
    private final int losses;
    
    // 랭킹 점수
    private final Integer score;
    
    // 랭킹 점수 계산
    public static int calculateScore(String tier, String rank, int lp) {
        int baseScore = switch (tier.toUpperCase()) {
            case "IRON" -> 0;
            case "BRONZE" -> 400;
            case "SILVER" -> 800;
            case "GOLD" -> 1200;
            case "PLATINUM" -> 1600;
            case "DIAMOND" -> 2000;
            case "MASTER" -> 2400;
            case "GRANDMASTER" -> 2800;
            case "CHALLENGER" -> 3200;
            default -> 0;
        };
        
        int rankScore = switch (rank) {
            case "IV" -> 0;
            case "III" -> 100;
            case "II" -> 200;
            case "I" -> 300;
            default -> 0;
        };
        
        return baseScore + rankScore + lp;
    }
}