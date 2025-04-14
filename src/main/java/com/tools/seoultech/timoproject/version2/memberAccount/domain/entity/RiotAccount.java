package com.tools.seoultech.timoproject.version2.memberAccount.domain.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class RiotAccount {
    private String puuid;
    private String accountName;
    private String accountTag;
}
