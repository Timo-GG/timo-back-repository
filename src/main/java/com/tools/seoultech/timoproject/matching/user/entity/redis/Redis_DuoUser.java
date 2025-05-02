package com.tools.seoultech.timoproject.matching.user.entity.redis;

import com.tools.seoultech.timoproject.matching.user.entity.embeddableType.DuoInfo_Ver2;
import com.tools.seoultech.timoproject.matching.user.entity.embeddableType.UserInfo_Ver2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Redis_DuoUser extends Redis_BaseUser {
    private UserInfo_Ver2 userInfo;
    private DuoInfo_Ver2 duoInfo;

    @Builder
    public Redis_DuoUser(Long memberId, UserInfo_Ver2 userInfo, DuoInfo_Ver2 duoInfo){
        super(memberId);
        this.userInfo = userInfo;
        this.duoInfo = duoInfo;
    }
}
