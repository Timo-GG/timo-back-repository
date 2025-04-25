package com.tools.seoultech.timoproject.version2.matching.domain.user.entity.mysql;

import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.DuoInfo_Ver2;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.UserInfo_Ver2;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DiscriminatorValue("Duo-Type")
@PrimaryKeyJoinColumn(name = "user_id")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DuoUser extends BaseUser {
    @Embedded
    private UserInfo_Ver2 userInfo;

    @Embedded
    private DuoInfo_Ver2 duoInfo;

    @Builder
    public DuoUser(MemberAccount member, UserInfo_Ver2 userInfo, DuoInfo_Ver2 duoInfo){
        super(member);
        this.userInfo = userInfo;
        this.duoInfo = duoInfo;
    }
}
