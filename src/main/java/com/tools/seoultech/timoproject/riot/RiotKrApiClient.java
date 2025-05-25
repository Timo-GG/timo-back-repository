package com.tools.seoultech.timoproject.riot;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Map;

@HttpExchange(url = "https://kr.api.riotgames.com", accept = MediaType.APPLICATION_JSON_VALUE)
public interface RiotKrApiClient {

    @GetExchange("/lol/champion-mastery/v4/champion-masteries/by-puuid/{puuid}")
    List<Map<String, Object>> getChampionMasteries(
            @PathVariable String puuid,
            @RequestHeader("X-Riot-Token") String apiKey
    );

    @GetExchange("/lol/league/v4/entries/by-puuid/{puuid}")
    List<Map<String, Object>> getRankInfo(
            @PathVariable String puuid,
            @RequestHeader("X-Riot-Token") String apiKey
    );

    @GetExchange("/lol/summoner/v4/summoners/by-puuid/{puuid}")
    Map<String, Object> getSummonerInfo(
            @PathVariable String puuid,
            @RequestHeader("X-Riot-Token") String apiKey
    );
}
