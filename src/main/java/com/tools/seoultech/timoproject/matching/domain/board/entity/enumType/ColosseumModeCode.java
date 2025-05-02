package com.tools.seoultech.timoproject.version2.matching.domain.board.entity.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.tools.seoultech.timoproject.version2.matching.domain.board.entity.enumType.ColosseumMapCode.ABYSS;
import static com.tools.seoultech.timoproject.version2.matching.domain.board.entity.enumType.ColosseumMapCode.RIFT;

@Getter
@RequiredArgsConstructor
public enum ColosseumModeCode {
    RIFT_VS_5(10, RIFT, 5),
    RIFT_VS_3(11, RIFT, 3),
    RIFT_VS_1(12, RIFT, 1),

    ABYSS_VS_5(20, ABYSS, 5),
    ABYSS_VS_3(21, ABYSS, 3),
    ABYSS_VS_1(22, ABYSS, 1);

    private final Integer code;
    private final ColosseumMapCode map;
    private final Integer num;
}
