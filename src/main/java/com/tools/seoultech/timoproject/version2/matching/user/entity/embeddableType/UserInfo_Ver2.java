package com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType;

import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayCondition;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayStyle;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.VoiceChat;
import jakarta.persistence.Embeddable;

@Embeddable
public class UserInfo_Ver2 {
    private PlayPosition myPosition;
    private PlayStyle myStyle;
    private PlayCondition myStatus;
    private VoiceChat myVoice;
}
