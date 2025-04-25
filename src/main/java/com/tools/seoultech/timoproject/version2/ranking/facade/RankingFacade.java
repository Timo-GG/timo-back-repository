package com.tools.seoultech.timoproject.version2.ranking.facade;

import com.tools.seoultech.timoproject.version2.ranking.dto.RankingUpdateRequestDto;
import com.tools.seoultech.timoproject.version2.ranking.dto.Redis_RankingInfo;

import java.util.List;

public interface RankingFacade {
    void createRanking(Long memberId, String puuid);
    void updateRankingInfo(Long memberId, RankingUpdateRequestDto rankingInfo);
    List<Redis_RankingInfo> getTopRankings(int limit);

}