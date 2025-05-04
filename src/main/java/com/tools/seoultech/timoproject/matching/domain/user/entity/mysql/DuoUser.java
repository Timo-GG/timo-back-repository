package com.tools.seoultech.timoproject.matching.domain.user.entity.mysql;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.DuoInfo_Ver2;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.UserInfo_Ver2;
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
    private UserInfo_Ver2 userInfo;

    @Embedded
    private DuoInfo_Ver2 duoInfo;

    @Builder
    public DuoUser(MemberAccount member, MatchingCategory category, UserInfo_Ver2 userInfo, DuoInfo_Ver2 duoInfo){
        super(member, category);
        this.userInfo = userInfo;
        this.duoInfo = duoInfo;
    }
}
