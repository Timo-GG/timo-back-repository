package com.tools.seoultech.timoproject.ranking.facade;

import com.tools.seoultech.timoproject.ranking.dto.RankingUpdateRequestDto;
import com.tools.seoultech.timoproject.ranking.dto.RedisRankingInfo;

import java.util.List;

public interface RankingFacade {
    void createRanking(Long memberId, String puuid);
    void updateRankingInfo(Long memberId, RankingUpdateRequestDto rankingInfo);
    void flushAllRedisRankings();
    void deleteRanking(Long memberId);
    List<RedisRankingInfo> getTopRankings(int offset, int limit);
    List<RedisRankingInfo> getTopRankingsByUniversity(String university, int offset, int limit);
    long getTotalRankingCount();
    long getTotalRankingCountByUniversity(String university);
    RedisRankingInfo getMyRankingInfo(Long memberId);
    int getRankingPosition(String name, String tag);
    void updateRankingFromRiotAPI(Long memberId);
    void updateAllRankingsFromRiotAPI();

}