package com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType;

import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayStyle;
import jakarta.persistence.Embeddable;

@Embeddable
public class DuoInfo_Ver2 {
    private PlayPosition opponentPosition;
    private PlayStyle opponentStyle;
}
