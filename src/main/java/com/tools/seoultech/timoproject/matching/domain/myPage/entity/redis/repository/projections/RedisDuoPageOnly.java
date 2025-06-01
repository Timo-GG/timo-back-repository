package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;

import java.time.LocalDateTime;
import java.util.UUID;

public interface RedisDuoPageOnly extends PageOnly {
    UUID getMyPageUUID();

    DuoMapCode getMapCode();

    MatchingCategory getMatchingCategory();

    CertifiedMemberInfo getAcceptorCertifiedMemberInfo();
    CertifiedMemberInfo getRequestorCertifiedMemberInfo();

    UserInfo getAcceptorUserInfo();
    UserInfo getRequestorUserInfo();

    String getAcceptorMemo();
    String getRequestorMemo();


    LocalDateTime getUpdatedAt();

    Long getAcceptorId();
    Long getRequestorId();
    UUID getBoardUUID();
}
