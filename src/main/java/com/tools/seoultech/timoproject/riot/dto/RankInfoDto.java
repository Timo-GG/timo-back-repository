package com.tools.seoultech.timoproject.riot.dto;

import java.util.Map;

/**
 * 랭크 정보를 담는 불변 데이터 객체 (Java Record)
 * tier: 티어 (GOLD, PLATINUM 등)
 * rank: 단계 (I, II, III, IV)
 * lp: 리그 포인트
 * wins: 승리 횟수
 * losses: 패배 횟수
 */
public record RankInfoDto(
        String tier,
        String rank,
        int lp,
        int wins,
        int losses
) {
    /**
     * Map 데이터로부터 RankInfoDto 객체를 생성합니다.
     * @param rankData 랭크 정보가 담긴 Map
     * @return 생성된 RankInfoDto 객체
     */
    public static RankInfoDto from(Map<String, Object> rankData) {
        // record는 new 키워드를 통해 canonical 생성자를 호출합니다.
        return new RankInfoDto(
                (String) rankData.get("tier"),
                (String) rankData.get("rank"),
                (Integer) rankData.get("leaguePoints"),
                (Integer) rankData.get("wins"),
                (Integer) rankData.get("losses")
        );
    }

    public static RankInfoDto unranked() {
        return new RankInfoDto("UNRANKED", "", 0, 0, 0);
    }
}