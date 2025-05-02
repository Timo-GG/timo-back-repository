package com.tools.seoultech.timoproject.riot.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record RiotRankingDto(
        List<String> most3ChampionNames,
        String profileIconUrl,
        RankInfoDto soloRankInfo,
        WinLossSummaryDto recentWinLossSummary
) {
    public static RiotRankingDto of(List<String> most3ChampionNames, String profileIconUrl, RankInfoDto soloRankInfo, WinLossSummaryDto recentWinLossSummary) {
        return new RiotRankingDto(most3ChampionNames, profileIconUrl, soloRankInfo, recentWinLossSummary);
    }

    public static RiotRankingDto of(RiotRankingDto riotRankingDto) {
        return new RiotRankingDto(riotRankingDto.most3ChampionNames(), riotRankingDto.profileIconUrl(), riotRankingDto.soloRankInfo(), riotRankingDto.recentWinLossSummary());

    }
}