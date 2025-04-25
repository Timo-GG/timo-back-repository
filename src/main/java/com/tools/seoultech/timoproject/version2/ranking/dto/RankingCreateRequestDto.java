package com.tools.seoultech.timoproject.version2.ranking.dto;

import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.RiotAccount;

public record RankingCreateRequestDto(
    RiotAccount riotAccount,
    RiotRankingDto riotRanking
) {
    public static RankingCreateRequestDto of(
            RiotAccount riotAccount,
            RiotRankingDto riotRanking) {
        return new RankingCreateRequestDto(riotAccount, riotRanking);
    }
}