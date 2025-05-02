package com.tools.seoultech.timoproject.riot.facade;

import com.tools.seoultech.timoproject.riot.dto.RankInfoDto;
import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;
import com.tools.seoultech.timoproject.riot.dto.WinLossSummaryDto;
import com.tools.seoultech.timoproject.riot.service.BasicAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiotFacadeImpl implements RiotFacade {

    private final BasicAPIService basicAPIService;

    @Override
    public RiotRankingDto getRiotRanking(String puuid) {
        List<String> most3ChampionNames = basicAPIService.getMost3ChampionNames(puuid);
        RankInfoDto soloRankInfoByPuuid = basicAPIService.getSoloRankInfoByPuuid(puuid);
        WinLossSummaryDto recentWinLossSummary = basicAPIService.getRecentWinLossSummary(puuid);
        String profileIconUrl = basicAPIService.getProfileIconUrlByPuuid(puuid);
        return RiotRankingDto.of(most3ChampionNames, profileIconUrl, soloRankInfoByPuuid, recentWinLossSummary);
    }
}
