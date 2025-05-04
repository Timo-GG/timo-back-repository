package com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType;

import com.tools.seoultech.timoproject.matching.domain.user.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.matching.domain.user.entity.enumType.PlayStyle;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DuoInfo_Ver2 {
    private PlayPosition opponentPosition;
    private PlayStyle opponentStyle;
}
