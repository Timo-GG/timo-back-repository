package com.tools.seoultech.timoproject.riot.dto;

import java.io.Serializable;
import java.util.List;

public record MatchSummaryDTO (
        String gameDuration,
        String playedAt,
        String gameMode,
        String championName,
        String championIconUrl,
        int championLevel,
        int kills,
        int deaths,
        int assists,
        boolean isWin,
        String minionsPerMinute,
        List<String> runes,
        List<String> summonerSpells,
        List<String> items
) implements Serializable {
    /**
     * record와 정적 팩토리 메서드의 조합
     * @param detail 원본 상세 매치 정보 DTO
     * @return 변환된 매치 요약 DTO
     */
    public static MatchSummaryDTO from(DetailMatchInfoDTO detail) {
        if (detail == null) {
            return null;
        }
        // record는 new 키워드를 통해 생성자를 직접 호출
        return new MatchSummaryDTO(
                detail.getTime(),
                detail.getLastGameEnd(),
                detail.getMode(),
                detail.getMyName(),
                detail.getIcon(),
                detail.getChampionLevel() != null ? detail.getChampionLevel() : 0,
                detail.getKills(),
                detail.getDeaths(),
                detail.getAssists(),
                detail.getWin(),
                detail.getMinionskilledPerMin(),
                List.of(detail.getRune3(), detail.getRune4()),
                List.of(detail.getSummoner1Id(), detail.getSummoner2Id()),
                detail.getItems()
        );
    }
}