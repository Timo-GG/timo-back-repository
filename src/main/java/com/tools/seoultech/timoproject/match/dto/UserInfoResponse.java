package com.tools.seoultech.timoproject.match.dto;

import com.tools.seoultech.timoproject.match.domain.UserInfo;
import lombok.Getter;

public record UserInfoResponse(
        String introduce,
        String gameMode,
        String playPosition,
        String playCondition,
        String voiceChat,
        String playStyle
) {
    public static UserInfoResponse from(UserInfo userInfo) {
        return new UserInfoResponse(
                userInfo.getIntroduce(),
                userInfo.getGameMode().name(),
                userInfo.getPlayPosition().name(),
                userInfo.getPlayCondition().name(),
                userInfo.getVoiceChat().name(),
                userInfo.getPlayStyle().name()
        );
    }
}