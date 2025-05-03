package com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType;

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
