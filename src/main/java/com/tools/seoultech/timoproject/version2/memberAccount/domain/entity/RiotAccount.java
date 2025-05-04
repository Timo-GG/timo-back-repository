package com.tools.seoultech.timoproject.version2.memberAccount.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiotAccount {
    private String puuid;
    private String accountName;
    private String accountTag;
}
