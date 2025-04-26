package com.tools.seoultech.timoproject.version2.ranking.facade;

import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;
import com.tools.seoultech.timoproject.riot.facade.RiotFacade;
import com.tools.seoultech.timoproject.version2.ranking.RankingInfo;
import com.tools.seoultech.timoproject.version2.ranking.dto.RankingCreateRequestDto;
import com.tools.seoultech.timoproject.version2.ranking.dto.RankingUpdateRequestDto;
import com.tools.seoultech.timoproject.version2.ranking.dto.Redis_RankingInfo;
import com.tools.seoultech.timoproject.version2.ranking.service.RankingRedisService;
import com.tools.seoultech.timoproject.version2.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RankingFacadeImpl implements RankingFacade {
    private final RankingService rankingService;
    private final RankingRedisService rankingRedisService;
    private final RiotFacade riotFacade;
    
    @Override
    public void createRanking(Long memberId, String puuid) {
        RiotRankingDto riotRanking = riotFacade.getRiotRanking(puuid);
        rankingRedisService.createInitialRanking(memberId, riotRanking);
    }

    @Override
    public void updateRankingInfo(Long memberId, RankingUpdateRequestDto dto) {
        rankingService.updateRankingInfo(memberId, dto);
        rankingRedisService.updateRankingInfo(memberId, dto);
    }

    @Override
    public List<Redis_RankingInfo> getTopRankings(int limit) {
        return rankingRedisService.getTopRankings(limit);
    }

    @Override
    public Redis_RankingInfo getMyRankingInfo(Long memberId) {
        return rankingRedisService.getMyRankingInfo(memberId);
    }
}