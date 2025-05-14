package com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType;

import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

@Embeddable
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class PartyMemberInfo {
    @Embedded
    private RiotAccount riotAccount;

    @Embedded
    private UserInfo userInfo;

    // #TODO: Compact Riot 전적 조회
    @Embedded
    private CompactPlayerHistory compactPlayerHistory;
}