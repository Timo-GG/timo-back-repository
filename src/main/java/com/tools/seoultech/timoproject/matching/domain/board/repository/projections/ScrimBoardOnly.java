package com.tools.seoultech.timoproject.matching.domain.board.repository.projections;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ScrimMapCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ScrimBoardOnly extends BoardOnly {
    ScrimMapCode getMapCode();
    String getMemo();
    Integer getHeadCount();

    List<CompactMemberInfo> getPartyInfo();

    LocalDateTime getUpdatedAt();
    Long getMemberId();
}
