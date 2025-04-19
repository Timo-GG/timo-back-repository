package com.tools.seoultech.timoproject.version2.ranking.facade;

import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;
import com.tools.seoultech.timoproject.riot.facade.RiotFacade;
import com.tools.seoultech.timoproject.version2.ranking.RankingInfo;
import com.tools.seoultech.timoproject.version2.ranking.dto.RankingCreateRequestDto;
import com.tools.seoultech.timoproject.version2.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RankingFacadeImpl implements RankingFacade {
    private final RankingService rankingService;
    private final RiotFacade riotFacade;
    
    @Override
    public void createRanking(Long memberId, String puuid) {
        RiotRankingDto riotRanking = riotFacade.getRiotRanking(puuid);
        rankingService.createRanking(memberId, riotRanking);
    }

    @Override
    public void updateRankingInfo(Long memberId, RankingInfo rankingInfo) {
        
    }
}