package com.tools.seoultech.timoproject.match.dto;

import com.tools.seoultech.timoproject.match.domain.UserInfo;
import lombok.Getter;

@Getter
public class UserInfoResponse {
    private String introduce;
    private String age;
    private String gender;
    private String playPosition;
    private String playCondition;
    private String voiceChat;
    private String playStyle;
    private String playTime;
    private String gameMode;

    public UserInfoResponse(UserInfo userInfo) {
        this.introduce = userInfo.getIntroduce();
        this.age = userInfo.getAge().name();
        this.gender = userInfo.getGender().name();
        this.playPosition = userInfo.getPlayPosition().name();
        this.playCondition = userInfo.getPlayCondition().name();
        this.voiceChat = userInfo.getVoiceChat().name();
        this.playStyle = userInfo.getPlayStyle().name();
        this.playTime = userInfo.getPlayTime().name();
        this.gameMode = userInfo.getGameMode().name();
    }
}