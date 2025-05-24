package com.tools.seoultech.timoproject.riot;

import com.tools.seoultech.timoproject.member.dto.AccountDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url = "https://asia.api.riotgames.com", accept = MediaType.APPLICATION_JSON_VALUE)
public interface RiotAsiaApiClient {

    @GetExchange("/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}")
    AccountDto.Response getAccountByRiotId(
            @PathVariable String gameName,
            @PathVariable String tagLine,
            @RequestHeader("X-Riot-Token") String apiKey
    );

    @GetExchange("/lol/match/v5/matches/{matchId}")
    String getMatchInfo(
            @PathVariable String matchId,
            @RequestHeader("X-Riot-Token") String apiKey
    );

    @GetExchange("/lol/match/v5/matches/by-puuid/{puuid}/ids")
    List<String> getMatchList(
            @PathVariable String puuid,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int count,
            @RequestHeader("X-Riot-Token") String apiKey
    );
}
