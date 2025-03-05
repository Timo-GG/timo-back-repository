package com.tools.seoultech.timoproject.match.dto;

import com.tools.seoultech.timoproject.match.domain.*;

public record DuoInfoRequest(
        PlayPosition duoPlayPosition,
        PlayStyle duoPlayStyle
) {

}