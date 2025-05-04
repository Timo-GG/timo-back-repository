package com.tools.seoultech.timoproject.matching.domain.user.entity.mysql;

import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType.RiotAccount;
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
    private List<RiotAccount> partyMemberList;

    @Builder
    public ColosseumUser(MemberAccount member, List<RiotAccount> partyMemberList) {
        super(member);  
        this.partyMemberList = partyMemberList;
    }
}
