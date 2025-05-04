package com.tools.seoultech.timoproject.riot.facade;

import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;

public interface RiotFacade {
    RiotRankingDto getRiotRanking(String puuid); // 랭킹에 필요한 라이엇 정보들
}
