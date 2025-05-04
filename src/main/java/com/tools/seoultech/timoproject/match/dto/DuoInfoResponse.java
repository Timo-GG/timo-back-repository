package com.tools.seoultech.timoproject.match.dto;

import com.tools.seoultech.timoproject.match.domain.DuoInfo;
import lombok.Getter;

@Getter
public class DuoInfoResponse {
    private String duoPlayPosition;
    private String duoPlayStyle;

    public DuoInfoResponse(DuoInfo duoInfo) {
        this.duoPlayPosition = duoInfo.getDuoPlayPosition().name();
        this.duoPlayStyle = duoInfo.getDuoPlayStyle().name();
    }
}