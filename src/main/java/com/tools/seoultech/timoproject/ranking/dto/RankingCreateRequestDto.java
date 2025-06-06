package com.tools.seoultech.timoproject.ranking.dto;

import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;

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