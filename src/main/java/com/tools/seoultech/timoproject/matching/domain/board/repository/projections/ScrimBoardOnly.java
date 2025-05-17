package com.tools.seoultech.timoproject.matching.domain.board.repository.projections;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ColosseumMapCode;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;

import java.util.List;
import java.util.UUID;

public interface ScrimBoardOnly {
    UUID getBoardUUID();
    ColosseumMapCode getMapCode();
    String getMemo();
    Integer getHeadCount();
    CertifiedMemberInfo getMemberInfo();
    List<PartyMemberInfo> getPartyInfo();
    Long getMemberId();
}
