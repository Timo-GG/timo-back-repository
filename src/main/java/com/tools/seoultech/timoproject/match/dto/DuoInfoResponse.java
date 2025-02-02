package com.tools.seoultech.timoproject.match.dto;

import com.tools.seoultech.timoproject.match.domain.DuoInfo;
import lombok.Getter;

@Getter
public class DuoInfoResponse {
    private String duoPlayPosition;
    private String duoPlayTime;
    private String duoVoiceChat;
    private String duoAge;

    public DuoInfoResponse(DuoInfo duoInfo) {
        this.duoPlayPosition = duoInfo.getDuoPlayPosition().name();
        this.duoPlayTime = duoInfo.getDuoPlayTime().name();
        this.duoVoiceChat = duoInfo.getDuoVoiceChat().name();
        this.duoAge = duoInfo.getDuoAge().name();
    }
}