package com.tools.seoultech.timoproject.match.dto;

import com.tools.seoultech.timoproject.match.domain.DuoInfo;

public record DuoInfoResponse(
        String duoPlayPosition,
        String duoPlayStyle
) {

    public static DuoInfoResponse of(String duoPlayPosition, String duoPlayStyle) {
        return new DuoInfoResponse(duoPlayPosition, duoPlayStyle);
    }

    public static DuoInfoResponse from(DuoInfo duoInfo) {
        return new DuoInfoResponse(duoInfo.getDuoPlayPosition().name(), duoInfo.getDuoPlayStyle().name());
    }
}