package com.tools.seoultech.timoproject.matching.domain.user.entity.mysql;

import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.DuoInfo;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.*;

@DiscriminatorValue("Duo-Type")
@PrimaryKeyJoinColumn(name = "user_id")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DuoUser extends BaseUser {
    @Embedded
    private UserInfo userInfo;

    @Embedded
    private DuoInfo duoInfo;

    @Builder
    public DuoUser(MemberAccount member, UserInfo userInfo, DuoInfo duoInfo){
        super(member);
        this.userInfo = userInfo;
        this.duoInfo = duoInfo;
    }
}
