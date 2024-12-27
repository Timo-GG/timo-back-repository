package com.tools.seoultech.timoproject.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DDragonSpellCode {
    SummonerBarrier("SummonerBarrier", 21, "배리어"),
    SummonerBoost("SummonerBoost", 1, "정화"),
    SummonerDot("SummonerDot", 14, "점화"),
    SummonerExhaust("SummonerExhaust", 3, "탈진"),
    SummonerFlash("SummonerFlash", 4, "점멸"),
    SummonerHaste("SummonerHaste", 6, "유체화"),
    SummonerHeal("SummonerHeal", 7, "힐"),
    SummonerTeleport("SummonerTeleport", 12, "텔레포트"),
    SummonerMana("SummonerMana", 13, "총명"),
    SummonerPoroRecall("SummonerPoroRecall", 30, "포로소환"),
    SummonerPoroThrow("SummonerPoroThrow", 31, "포로던지기"),
    SummonerSmite("SummonerSmite", 11, "강타"),
    SummonerSnowURFSnowball_Mark("SummonerSnowURFSnowball_Mark", 39, "URF표식"),
    SummonerSnowball("SummonerSnowball", 32, "표식");

    private final String name;
    private final int key;
    private final String comment;

    public static DDragonSpellCode of(Integer key) {
        DDragonSpellCode[] values = DDragonSpellCode.values();
        for(int var=0; var < values.length; ++var){
            if(key == values[var].key) return values[var];
        }
        return null;
    }
    public static DDragonSpellCode of(String comment){
        DDragonSpellCode[] values = DDragonSpellCode.values();
        for(int var=0; var < values.length; ++var){
            if(comment == values[var].comment) return values[var];
        }
        return null;
    }
}
