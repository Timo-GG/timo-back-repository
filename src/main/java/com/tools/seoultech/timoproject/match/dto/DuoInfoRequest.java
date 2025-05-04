package com.tools.seoultech.timoproject.match.dto;

import com.tools.seoultech.timoproject.match.domain.*;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayStyle;

public record DuoInfoRequest(
        PlayPosition duoPlayPosition,
        PlayStyle duoPlayStyle
) {

}