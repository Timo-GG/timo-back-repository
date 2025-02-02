package com.tools.seoultech.timoproject.match.dto;

import com.tools.seoultech.timoproject.match.domain.Age;
import com.tools.seoultech.timoproject.match.domain.PlayPosition;
import com.tools.seoultech.timoproject.match.domain.PlayTime;
import com.tools.seoultech.timoproject.match.domain.VoiceChat;
import lombok.Getter;

import java.util.Set;

@Getter
public class DuoInfoRequest {
    private PlayPosition duoPlayPosition;
    private PlayTime duoPlayTime;
    private VoiceChat duoVoiceChat;
    private Age duoAge;
}