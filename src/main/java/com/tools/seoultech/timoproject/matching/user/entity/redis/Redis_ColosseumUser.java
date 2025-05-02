package com.tools.seoultech.timoproject.version2.matching.user.entity.redis;

import com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType.PartyMemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Redis_ColosseumUser extends Redis_BaseUser {
    private List<PartyMemberInfo> partyMemberInfoList;

    @Builder
    public Redis_ColosseumUser(Long memberId, List<PartyMemberInfo> partyMemberInfoList) {
        super(memberId);
        this.partyMemberInfoList = partyMemberInfoList;
    }
}
