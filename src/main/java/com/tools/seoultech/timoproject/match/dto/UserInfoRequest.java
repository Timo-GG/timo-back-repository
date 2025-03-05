package com.tools.seoultech.timoproject.match.dto;

import com.tools.seoultech.timoproject.match.domain.*;

public record UserInfoRequest(
        String introduce,
        GameMode gameMode,
        PlayPosition playPosition,
        PlayCondition playCondition,
        VoiceChat voiceChat,
        PlayStyle playStyle
) {

}
