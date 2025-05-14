package com.tools.seoultech.timoproject.matching.domain.board.entity.repository.projections;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactPlayerHistory;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.*;

import java.util.UUID;

public interface DuoBoardOnly {
    UUID getBoardUUID();
    DuoMapCode getDuoMapCode();
    String getMapCode();
    CompactPlayerHistory getCompactPlayerHistory();

    PlayPosition getMyPosition();
    VoiceChat getMyVoice();
    PlayStyle getMyStyle();
    PlayCondition getMyStatus();

    PlayPosition getOpponentPosition();
    PlayStyle getOpponentStyle();
}
