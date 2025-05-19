package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;

import java.util.UUID;

public interface DuoMyPageOnly {
    UUID getMyPageUUID();

    DuoMapCode getMapCode();

    CertifiedMemberInfo getAcceptorCertifiedMemberInfo();
    CertifiedMemberInfo getRequesterCertifiedMemberInfo();
    UserInfo getAcceptorUserInfo();
    UserInfo getRequestorUserInfo();

    Long getAcceptorId();
    Long getRequestorId();

    UUID getBoardUUID();
}
