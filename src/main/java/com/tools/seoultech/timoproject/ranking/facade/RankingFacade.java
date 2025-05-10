package com.tools.seoultech.timoproject.ranking.facade;

import com.tools.seoultech.timoproject.ranking.dto.RankingUpdateRequestDto;
import com.tools.seoultech.timoproject.ranking.dto.RedisRankingInfo;

import java.util.List;

public interface RankingFacade {
    void createRanking(Long memberId, String puuid);
    void updateRankingInfo(Long memberId, RankingUpdateRequestDto rankingInfo);
    void flushAllRedisRankings();
    void deleteRanking(Long memberId);
    List<RedisRankingInfo> getTopRankings(int limit);
    List<RedisRankingInfo> getTopRankingsByUniversity(String university, int limit);
    RedisRankingInfo getMyRankingInfo(Long memberId);
}