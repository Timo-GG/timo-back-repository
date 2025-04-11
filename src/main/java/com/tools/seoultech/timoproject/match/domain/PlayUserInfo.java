package com.tools.seoultech.timoproject.match.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class PlayUserInfo {
    private String puuid;
    private String userName;
    private String userTag;
}
