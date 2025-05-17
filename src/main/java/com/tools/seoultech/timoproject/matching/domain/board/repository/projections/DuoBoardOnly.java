package com.tools.seoultech.timoproject.matching.domain.board.repository.projections;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.*;

import java.util.UUID;

public interface DuoBoardOnly {
    UUID getBoardUUID();
    DuoMapCode getMapCode();
    String getMemo();
    CertifiedMemberInfo getMemberInfo();

    PlayPosition getMyPosition();
    VoiceChat getMyVoice();
    PlayStyle getMyStyle();
    PlayCondition getMyStatus();

    PlayPosition getOpponentPosition();
    PlayStyle getOpponentStyle();

    Long getMemberId();
}
