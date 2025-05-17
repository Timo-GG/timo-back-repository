package com.tools.seoultech.timoproject.matching.domain.myPage.repository.projections;


import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;


import java.util.List;
import java.util.UUID;

public interface ScrimMyPageOnly {
    UUID getMyPageUUID();

    CertifiedMemberInfo getAcceptorCertifiedMemberInfo();
    CertifiedMemberInfo getRequestorCertifiedMemberInfo();
    List<PartyMemberInfo> getAcceptorPartyInfo();
    List<PartyMemberInfo> getRequestorPartyInfo();
}
