package com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType;

import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayStyle;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class DuoInfo_Ver2 {
    @Enumerated(value = EnumType.STRING)
    private PlayPosition opponentPosition;
    @Enumerated(value = EnumType.STRING)
    private PlayStyle opponentStyle;
}
