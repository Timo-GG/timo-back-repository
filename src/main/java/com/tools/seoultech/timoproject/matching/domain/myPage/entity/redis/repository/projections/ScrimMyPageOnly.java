package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ScrimMapCode;

import java.util.List;
import java.util.UUID;


public interface ScrimMyPageOnly extends MyPageOnly{
    UUID getMyPageUUID();

    Integer getHeadCount();
    ScrimMapCode getMapCode();

    CertifiedMemberInfo getAcceptorCertifiedMemberInfo();
    CertifiedMemberInfo getRequestorCertifiedMemberInfo();
    List<PartyMemberInfo> getAcceptorPartyInfo();
    List<PartyMemberInfo> getRequestorPartyInfo();

    Long getAcceptorId();
    Long getRequestorId();
}
