package com.tools.seoultech.timoproject.match.dto;

import com.tools.seoultech.timoproject.match.domain.DuoInfo;
import com.tools.seoultech.timoproject.match.domain.UserInfo;

public record MatchingOptionRequest(UserInfoRequest userInfo, DuoInfoRequest duoInfo) {

    public UserInfo toUserInfo() {
        return UserInfo.builder()
                .introduce(userInfo.introduce())
                .gameMode(userInfo.gameMode())
                .playPosition(userInfo.playPosition())
                .playCondition(userInfo.playCondition())
                .voiceChat(userInfo.voiceChat())
                .playStyle(userInfo.playStyle())
                .build();
    }

    public DuoInfo toDuoInfo() {
        return DuoInfo.builder()
                .duoPlayPosition(duoInfo.duoPlayPosition())
                .duoPlayStyle(duoInfo.duoPlayStyle())
                .build();
    }
}
