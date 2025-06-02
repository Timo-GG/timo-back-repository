package com.tools.seoultech.timoproject.riot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchSummaryDTO {
    private String gameDuration;
    private String playedAt;
    private String gameMode; // 일반 / 랭크 / 칼바람
    private String championName;
    private String championIconUrl;
    private int championLevel;
    private int kills;
    private int deaths;
    private int assists;
    private boolean isWin;
    private String minionsPerMinute;
    private List<String> runes;
    private List<String> summonerSpells;
    private List<String> items;
}