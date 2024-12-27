package com.tools.seoultech.timoproject.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DDragonModeIdCode {
    Custom(0, "Custom", "사용자 설정"),
    Bot_Intro(870, "Summoner's Rift", "입문 봇"),
    Bot_Biginner(880, "Summoner's Rift", "초급 봇"),
    Bot_Intermmediate(890, "Summoner's Rift", "중급 봇"),
    Normal_SummonersRift(490, "Summoner's Rift", "일반"),
    SodoDuoRank_SummonerRift(420, "Summoner's Rift", "단일/듀오 랭크"),
    DuoRank_SummonersRift(440, "Summoner's Rift", "자유랭크 게임"),
    HowlingAbyss(450, "Howling Abyss", "무직위 총력전"),
    Other(-1, "Other Mode", "기타 모드")
    ;

    private final int queueId;
    private final String map;
    private final String mode;

    public static DDragonModeIdCode of(Integer queueId) {
        DDragonModeIdCode[] values = DDragonModeIdCode.values();
        for(int var=0; var < values.length; ++var){
            if(queueId == values[var].queueId) return values[var];
        }
        return DDragonModeIdCode.Other;
    }
}
