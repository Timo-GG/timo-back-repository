package com.tools.seoultech.timoproject.version2.matching.user.entity;

import com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType.UserInfo_Ver2;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@DiscriminatorValue("Colosseum-Type")
@PrimaryKeyJoinColumn(name = "user_id")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ColosseumUserEntity extends BaseUserEntity{
    @ElementCollection
    private List<PartyMemberInfo> partyMemberList;

    @Builder
    public ColosseumUserEntity(MemberAccount member, List<PartyMemberInfo> partyMemberList) {
        super(member);
        this.partyMemberList = partyMemberList;
    }
}
