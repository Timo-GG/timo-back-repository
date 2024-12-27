package com.tools.seoultech.timoproject.dto;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.tools.seoultech.timoproject.constant.DDragonModeIdCode;
import com.tools.seoultech.timoproject.constant.DDragonSpellCode;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.minidev.json.JSONArray;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class Detail_MatchInfoDTO {
    private final String myName;
    private final String myTag;
    private final String icon;

    private final String mode;
    private final Boolean win;
    private final String time;
    private final String lastGameEnd;

    private final Integer kills;
    private final Integer deaths;
    private final Integer assists;
    private final Integer totalKillsPencentage;
    private final String multiKill;
    private final Boolean mvpYes;

    private final String summoner1Id; // TODO: id to url img
    private final String summoner2Id; // TODO: "
    private final String rune3; // TODO: "
    private final String rune4; // TODO: "

    private final List<String> items; // TODO: 0번 아이템 비즈니스 로직
    private final Boolean surren;

    private final Integer totalMinionskilled;
    private final String minionskilledPerMin;
    private final List<List<String>> participants; // TODO: puuid to riotIdGameName

    public static Detail_MatchInfoDTO of(MatchInfoDTO matchInfoDTO, String puuid, String runeJson){
        MatchInfoDTO.UserInfo myInfo = matchInfoDTO.getMyInfo(puuid);
        DocumentContext node = JsonPath.parse(runeJson);
        String jsonPath1 = String.format("$[*].slots[0]..runes[?(@.id==%d)]['icon']", myInfo.getRunes().get(0));
        String jsonPath2 = String.format("$..[*][?(@.id==%d)]['icon']", myInfo.getRunes().get(1));

        LocalTime time = LocalTime.ofSecondOfDay(matchInfoDTO.getGameDuration());
        String timeString = String.format("%d분 %d초", time.getMinute(), time.getSecond());

        String lastTimeFormat;
        long timeDifferenceInSeconds = (long)Instant.now().getEpochSecond() - (matchInfoDTO.getGameEndTimestamp()/1000);


        long days = (long)Math.round((double)timeDifferenceInSeconds / (24 * 60 * 60)); // 일 계산
        long hours = (timeDifferenceInSeconds % (24 * 60 * 60)) / 3600; // 시간 계산
        long minutes = (timeDifferenceInSeconds % 3600) / 60; // 분 계산
        long seconds = timeDifferenceInSeconds % 60;

//        lastTimeFormat = days+"/"+hours+"/"+minutes+"/"+seconds;
        if(days>0) lastTimeFormat = days+"일 전";
        else if(days==0&&hours>0) lastTimeFormat=hours+"시간 "+minutes+"분 전";
        else if(days==0&&hours==0) lastTimeFormat=minutes+"분 "+seconds+"초 전";
        else lastTimeFormat="시간경과";




        Double csString = 60 * myInfo.getTotalMinionsKilled().doubleValue() / matchInfoDTO.getGameDuration();
        System.out.println(csString);
        String csPerMin = String.format("%.1fCS / 분", csString);

        int totalKillSum=0;
        if (myInfo.getTeamId() == 100) {
            totalKillSum = IntStream.rangeClosed(0, 4)
                    .map(i -> matchInfoDTO.getUserInfo().get(i).getKills())
                    .sum();
        }
        else{
            totalKillSum = IntStream.rangeClosed(5, 9)
                    .map(i -> matchInfoDTO.getUserInfo().get(i).getKills())
                    .sum();
        }
        totalKillSum = (totalKillSum>0) ? (myInfo.getAssists() + myInfo.getKills())*100/totalKillSum : 0;
        Boolean mvpBool = false;
        if(myInfo.getDeaths()!=0) mvpBool = ((myInfo.getKills()+myInfo.getAssists())/(1.0*myInfo.getDeaths()) >= 3.5) ? true:false;
        else if(myInfo.getDeaths()==0 && (myInfo.getKills()+myInfo.getAssists())!=0) mvpBool = true;


//        String iconURL = String.format("https://ddragon.leagueoflegends.com/cdn/14.23.1/img/champion/%s.png", myInfo.getChampionName());
        String iconURL = String.format("https://ddragon.leagueoflegends.com/cdn/img/champion/tiles/%s_0.jpg", myInfo.getChampionName());
        List<String> itemURL = myInfo.getItems().stream()
                .map(id -> (id>0) ? String.format("https://ddragon.leagueoflegends.com/cdn/14.23.1/img/item/%d.png", id) : null)
                .collect(Collectors.toList());
        String spell1URL = String.format("https://ddragon.leagueoflegends.com/cdn/14.23.1/img/spell/%s.png",
                DDragonSpellCode.of(myInfo.getSummoner1Id()));
        String spell2URL = String.format("https://ddragon.leagueoflegends.com/cdn/14.23.1/img/spell/%s.png",
                DDragonSpellCode.of(myInfo.getSummoner2Id()));
        String mainRuneURL = String.format("https://ddragon.leagueoflegends.com/cdn/img/%s", ((JSONArray)node.read(jsonPath1)).getFirst());
        String subRuneURL = String.format("https://ddragon.leagueoflegends.com/cdn/img/%s", ((JSONArray)node.read(jsonPath2)).getFirst());
        List<List<String>> participants = matchInfoDTO.getUserInfo()
                .stream()
                .map(user -> {
                        return List.of(
                            user.getRiotIdGameName(),
                            String.format("https://ddragon.leagueoflegends.com/cdn/img/champion/tiles/%s_0.jpg", user.getChampionName()));})
                .collect(Collectors.toList());
        Detail_MatchInfoDTO var =  Detail_MatchInfoDTO.builder()
                .myName(myInfo.getRiotIdGameName())
                .myTag(myInfo.getRiotIdTagLine())
                .icon(iconURL)
                .mode(DDragonModeIdCode.of(matchInfoDTO.getQueueId()).getMode())
                .win(myInfo.getWin())
                .time(timeString)
                .lastGameEnd(lastTimeFormat)
                .kills(myInfo.getKills())
                .deaths(myInfo.getDeaths())
                .assists(myInfo.getAssists())
                .totalKillsPencentage(totalKillSum)
                .multiKill(myInfo.getMultiKill())
                .mvpYes(mvpBool)
                .summoner1Id(spell1URL)
                .summoner2Id(spell2URL)
                .rune3(mainRuneURL)
                .rune4(subRuneURL)
                .items(itemURL)
                .totalMinionskilled(myInfo.getTotalMinionsKilled())
                .minionskilledPerMin(csPerMin)
                .participants(participants)
                .surren(myInfo.getGameEndedInEarlySurrender())
                .build();
        return var;
    }
}



