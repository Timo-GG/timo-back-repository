package com.tools.seoultech.timoproject.version2.ranking.facade;

import com.tools.seoultech.timoproject.version2.ranking.RankingInfo;

public interface RankingFacade {
    void createRanking(Long memberId, String puuid);
    void updateRankingInfo(Long memberId, RankingInfo rankingInfo);
}