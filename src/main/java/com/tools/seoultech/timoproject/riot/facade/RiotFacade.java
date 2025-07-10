package com.tools.seoultech.timoproject.riot.facade;

import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.riot.dto.RecentMatchFullResponse;
import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;

public interface RiotFacade {
    RiotRankingDto getRiotRanking(String puuid);
    RecentMatchFullResponse getRecentMatchFullResponse(AccountDto.Request request);
}
