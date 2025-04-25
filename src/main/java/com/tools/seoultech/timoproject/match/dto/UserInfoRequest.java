package com.tools.seoultech.timoproject.match.dto;

import com.tools.seoultech.timoproject.match.domain.*;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.enumType.PlayCondition;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.enumType.PlayStyle;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.enumType.VoiceChat;

public record UserInfoRequest(
        String introduce,
        GameMode gameMode,
        PlayPosition playPosition,
        PlayCondition playCondition,
        VoiceChat voiceChat,
        PlayStyle playStyle
) {

}
