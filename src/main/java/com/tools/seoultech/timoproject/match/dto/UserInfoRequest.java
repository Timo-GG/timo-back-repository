package com.tools.seoultech.timoproject.match.dto;

import com.tools.seoultech.timoproject.match.domain.*;
import lombok.Getter;

@Getter
public class UserInfoRequest {
    private String introduce;
    private Age age;
    private Gender gender;
    private PlayPosition playPosition;
    private PlayCondition playCondition;
    private VoiceChat voiceChat;
    private PlayStyle playStyle;
    private PlayTime playTime;
    private GameMode gameMode;
}
