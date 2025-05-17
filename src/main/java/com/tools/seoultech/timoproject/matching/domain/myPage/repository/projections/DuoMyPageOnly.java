package com.tools.seoultech.timoproject.matching.domain.myPage.repository.projections;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.DuoInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;

import java.util.UUID;

public interface DuoMyPageOnly {
    UUID getMyPageUUID();

    CertifiedMemberInfo getAcceptorCertifiedMemberInfo();
    CertifiedMemberInfo getRequesterCertifiedMemberInfo();
    UserInfo getAcceptorUserInfo();
    UserInfo getRequestorUserInfo();


}
