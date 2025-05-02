package com.tools.seoultech.timoproject.matching.user.entity.mysql;

import com.tools.seoultech.timoproject.matching.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.memberAccount.domain.MemberAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@DiscriminatorValue("Colosseum-Type")
@PrimaryKeyJoinColumn(name = "user_id")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ColosseumUser extends BaseUser {
    @ElementCollection
    private List<PartyMemberInfo> partyMemberList;

    @Builder
    public ColosseumUser(MemberAccount member, List<PartyMemberInfo> partyMemberList) {
        super(member);
        this.partyMemberList = partyMemberList;
    }
}
