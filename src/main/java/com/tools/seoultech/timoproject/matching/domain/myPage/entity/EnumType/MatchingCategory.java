package com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum MatchingCategory {
    DUO,
    COLOSSEUM
}
