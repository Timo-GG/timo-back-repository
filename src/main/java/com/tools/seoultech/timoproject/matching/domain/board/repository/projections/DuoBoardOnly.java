package com.tools.seoultech.timoproject.matching.domain.board.repository.projections;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.*;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DuoBoardOnly extends BoardOnly{
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

    LocalDateTime getUpdatedAt();

    Long getMemberId();
}
