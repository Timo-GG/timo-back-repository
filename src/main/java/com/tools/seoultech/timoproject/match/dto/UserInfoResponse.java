package com.tools.seoultech.timoproject.match.dto;

import com.tools.seoultech.timoproject.match.domain.UserInfo;
import lombok.Getter;

@Getter
public class UserInfoResponse {
    private String introduce;
    private String gameMode;
    private String playPosition;
    private String playCondition;
    private String voiceChat;
    private String playStyle;

    public UserInfoResponse(UserInfo userInfo) {
        this.introduce = userInfo.getIntroduce();
        this.gameMode = userInfo.getGameMode().name();
        this.playPosition = userInfo.getPlayPosition().name();
        this.playCondition = userInfo.getPlayCondition().name();
        this.voiceChat = userInfo.getVoiceChat().name();
        this.playStyle = userInfo.getPlayStyle().name();
    }
}