package com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType;

import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.RiotAccount;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyMemberInfo {
    @Embedded
    private RiotAccount riotAccount;

    @Embedded
    private UserInfo_Ver2 userInfo;

    // #TODO: Compact Riot 전적 조회
    @Embedded
    private CompactPlayerHistory compactPlayerHistory;
}