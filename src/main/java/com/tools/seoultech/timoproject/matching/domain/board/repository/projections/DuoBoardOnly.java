package com.tools.seoultech.timoproject.matching.domain.board.repository.projections;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DuoBoardOnly extends BoardOnly{
    DuoMapCode getMapCode();
    String getMemo();

    /** UserInfo */
    VoiceChat getMyVoice();
    PlayStyle getMyStyle();
    PlayCondition getMyStatus();

    /** duoInfo */
    PlayPosition getOpponentPosition();
    PlayStyle getOpponentStyle();

    LocalDateTime getUpdatedAt();
    Long getMemberId();
}
