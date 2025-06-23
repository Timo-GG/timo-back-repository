package com.tools.seoultech.timoproject.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RiotAccountInfo {

    @JsonProperty("puuid")
    private String puuid;

    @JsonProperty("gameName")
    private String gameName;

    @JsonProperty("tagLine")
    private String tagLine;

    private String profileIconUrl;

    public RiotAccountInfo withProfileIconUrl(String profileIconUrl) {
        return new RiotAccountInfo(this.puuid, this.gameName, this.tagLine, profileIconUrl);
    }

    public String getFullGameName() {
        return gameName + "#" + tagLine;
    }

}
