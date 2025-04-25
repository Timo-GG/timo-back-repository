package com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType;

import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayCondition;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayStyle;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.VoiceChat;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo_Ver2 {

    @Enumerated(value = EnumType.STRING)
    private PlayPosition myPosition;
    @Enumerated(value = EnumType.STRING)
    private PlayStyle myStyle;
    @Enumerated(value = EnumType.STRING)
    private PlayCondition myStatus;
    @Enumerated(value = EnumType.STRING)
    private VoiceChat myVoice;
}
