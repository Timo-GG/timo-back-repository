package com.tools.seoultech.timoproject.ranking.facade;

import com.tools.seoultech.timoproject.ranking.dto.RankingUpdateRequestDto;
import com.tools.seoultech.timoproject.ranking.dto.Redis_RankingInfo;

import java.util.List;

public interface RankingFacade {
    void createRanking(Long memberId, String puuid);
    void updateRankingInfo(Long memberId, RankingUpdateRequestDto rankingInfo);
    void flushAllRedisRankings();
    void deleteRanking(Long memberId);
    List<Redis_RankingInfo> getTopRankings(int limit);
    List<Redis_RankingInfo> getTopRankingsByUniversity(String university, int limit);
    Redis_RankingInfo getMyRankingInfo(Long memberId);
}