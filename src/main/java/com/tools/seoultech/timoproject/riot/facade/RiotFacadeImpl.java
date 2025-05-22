package com.tools.seoultech.timoproject.riot.facade;

import com.tools.seoultech.timoproject.riot.dto.RankInfoDto;
import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;
import com.tools.seoultech.timoproject.riot.dto.WinLossSummaryDto;
import com.tools.seoultech.timoproject.riot.service.RiotAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiotFacadeImpl implements RiotFacade {

    private final RiotAPIService riotAPIService;

    @Override
    public RiotRankingDto getRiotRanking(String puuid) {
        List<String> most3ChampionNames = riotAPIService.getMost3ChampionNames(puuid);
        RankInfoDto soloRankInfoByPuuid = riotAPIService.getSoloRankInfoByPuuid(puuid);
        WinLossSummaryDto recentWinLossSummary = riotAPIService.getRecentWinLossSummary(puuid);
        String profileIconUrl = riotAPIService.getProfileIconUrlByPuuid(puuid);
        return RiotRankingDto.of(most3ChampionNames, profileIconUrl, soloRankInfoByPuuid, recentWinLossSummary);
    }
}
