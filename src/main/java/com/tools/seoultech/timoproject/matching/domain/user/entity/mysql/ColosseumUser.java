package com.tools.seoultech.timoproject.matching.domain.user.entity.mysql;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType.RiotAccount;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@DiscriminatorValue("Colosseum-Type")
@PrimaryKeyJoinColumn(name = "user_id")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ColosseumUser extends BaseUser {
    @ElementCollection
    private List<RiotAccount> partyMemberList;

    @Builder
    public ColosseumUser(MemberAccount member, List<RiotAccount> partyMemberList) {
        super(member);
        this.partyMemberList = partyMemberList;
    }
}
