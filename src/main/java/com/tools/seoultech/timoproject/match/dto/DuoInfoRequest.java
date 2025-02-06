package com.tools.seoultech.timoproject.match.dto;

import com.tools.seoultech.timoproject.match.domain.*;
import lombok.Getter;

import java.util.Set;

@Getter
public class DuoInfoRequest {
    private PlayPosition duoPlayPosition;
    private PlayStyle duoPlayStyle;
}