package com.tools.seoultech.timoproject.match.dto;

import com.tools.seoultech.timoproject.match.domain.MatchingOption;
import lombok.Getter;

@Getter
public class MatchingOptionResponse {
    private Long id;
    private String introduce;
    private String age;
    private String gender;
    private String playPosition;
    private String playCondition;
    private String voiceChat;
    private String playStyle;
    private String playTime;
    private String gameMode;

    public MatchingOptionResponse(MatchingOption matchingOption) {
        this.id = matchingOption.getId();
        this.introduce = matchingOption.getIntroduce();
        this.age = matchingOption.getAge().name();
        this.gender = matchingOption.getGender().name();
        this.playPosition = matchingOption.getPlayPosition().name();
        this.playCondition = matchingOption.getPlayCondition().name();
        this.voiceChat = matchingOption.getVoiceChat().name();
        this.playStyle = matchingOption.getPlayStyle().name();
        this.playTime = matchingOption.getPlayTime().name();
        this.gameMode = matchingOption.getGameMode().name();
    }
}
