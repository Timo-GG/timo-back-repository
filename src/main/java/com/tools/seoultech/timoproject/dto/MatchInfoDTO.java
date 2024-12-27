package com.tools.seoultech.timoproject.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.tools.seoultech.timoproject.exception.RiotAPIException;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@RequiredArgsConstructor
public class MatchInfoDTO {
    private final List<String> participants;
    private final Integer queueId;
    private final String gameMode;
    private final Long gameDuration;
    private final Long gameEndTimestamp;

    private final List<UserInfo> userInfo;
//    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(builder = UserInfo.UserInfoBuilder.class)
    @Getter
    @ToString
    @Jacksonized
    public static class UserInfo {
        private final String puuid;
        private final String riotIdGameName;
        private final String riotIdTagLine; // riotIdTagline

        private final String championName;

        private final Integer kills;
        private final Integer deaths;
        private final Integer assists;

        private final String multiKill;
        private final Integer totalMinionsKilled;
        private final Integer summoner1Id;
        private final Integer summoner2Id;

        @Size(min=6, max=6)
        private final List<Integer> Items;

            @Size(min=2, max=2)
//            @JsonDeserialize(using=MatchInfoDeserializer.class)
            private final List<Integer> Runes;

        private final Boolean gameEndedInEarlySurrender;
        private final Integer teamId;
        private final Boolean win;

        @JsonCreator
        public UserInfo(
                @JsonProperty("puuid") String puuid,
                @JsonProperty("riotIdGameName") String riotIdGameName,
                @JsonProperty("riotIdTagline") String riotIdTagLine,
                @JsonProperty("championName") String championName,
                @JsonProperty("kills") Integer kills,
                @JsonProperty("deaths") Integer deaths,
                @JsonProperty("assists") Integer assists,
                @JsonProperty("totalMinionsKilled") Integer totalMinionsKilled,
                @JsonProperty("summoner1Id") Integer summoner1Id,
                @JsonProperty("summoner2Id") Integer summoner2Id,
                @JsonProperty("item0") Integer item0,
                @JsonProperty("item1") Integer item1,
                @JsonProperty("item2") Integer item2,
                @JsonProperty("item3") Integer item3,
                @JsonProperty("item4") Integer item4,
                @JsonProperty("item5") Integer item5,
                @JsonProperty("item6") Integer item6,
                @JsonProperty("perks") JSONObject perks,
                @JsonProperty("teamId") Integer teamId,
                @JsonProperty("win") Boolean win,
                @JsonProperty("doubleKills") Integer doubleKillB,
                @JsonProperty("tripleKills") Integer tripleKillB,
                @JsonProperty("quadraKills") Integer quadraKillB,
                @JsonProperty("pentaKills") Integer pentaKills,
                @JsonProperty("gameEndedInEarlySurrender") Boolean surrender
        ) {

            this.puuid = puuid;
            this.riotIdGameName = riotIdGameName;
            this.riotIdTagLine = riotIdTagLine;
            this.championName = championName;

            this.kills = kills;
            this.deaths = deaths;
            this.assists = assists;


            if(pentaKills>0) this.multiKill="펜타킬";
            else if(quadraKillB>0) this.multiKill="쿼드라킬";
            else if(tripleKillB>0) this.multiKill="트리플킬";
            else if(doubleKillB>0) this.multiKill="더블킬";
            else this.multiKill=null;

            this.totalMinionsKilled = totalMinionsKilled;
            this.summoner1Id = summoner1Id;
            this.summoner2Id = summoner2Id;
            this.Items = List.of(item0, item1, item2, item3, item4, item5, item6);
//                    .stream()
//                    .filter(item -> item > 0)
//                    .collect(Collectors.toList());

            DocumentContext node = JsonPath.parse(perks);
            Integer mainRune = node.read("$.styles[0].selections[0].perk");
            Integer subRune = node.read("$.styles[1].style");
            this.Runes = List.of(mainRune, subRune);
            this.gameEndedInEarlySurrender = surrender;
            this.teamId = teamId;
            this.win = win;
        }
    }

    public static MatchInfoDTO of(String json) {
        Configuration conf = Configuration
                .builder()
                .mappingProvider(new JacksonMappingProvider())
                .build();
        TypeRef<List<UserInfo>> typeRef = new TypeRef<>() {};

        DocumentContext node = JsonPath.using(conf).parse(json);
        MatchInfoDTO testDTO = new MatchInfoDTO(
                node.read("$.metadata.participants"),
                node.read("$.info.queueId"),
                node.read("$.info.gameMode"),
                node.read("$.info.gameDuration", Long.class),
                node.read("$.info.gameEndTimestamp", Long.class),
                node.read("$.info.participants.*['puuid',  'riotIdGameName', 'riotIdTagline', 'championName'," +
                        " 'kills', 'deaths', 'assists', 'totalMinionsKilled', 'summoner1Id', 'summoner2Id', " +
                        "'item0', 'item1', 'item2', 'item3', 'item4', 'item5', 'item6', " +
                        "'doubleKills', 'tripleKills','quadraKills', 'pentaKills','perks', 'gameEndedInEarlySurrender','teamId', 'win']", typeRef)
        );
        return testDTO;
    }

    public UserInfo getMyInfo(String puuid) {
        return userInfo.stream()
                .filter(u -> u.getPuuid().equals(puuid))
                .findFirst()
                .orElseThrow(() -> new RiotAPIException("대전 유저 중에 일치하는 puuid가 없습니다."));
    }
}
