package com.tools.seoultech.timoproject.matching.domain.board.repository.projections;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ScrimMapCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ScrimBoardOnly extends BoardOnly {
    UUID getBoardUUID();
    ScrimMapCode getMapCode();
    String getMemo();
    LocalDateTime getUpdatedAt();
    Integer getHeadCount();
    CertifiedMemberInfo getMemberInfo();
    List<PartyMemberInfo> getPartyInfo();
    Long getMemberId();
}
