package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ScrimMapCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface RedisScrimPageOnly extends PageOnly {
    UUID getMyPageUUID();

    Integer getHeadCount();
    ScrimMapCode getMapCode();
    MatchingCategory getMatchingCategory();

    CertifiedMemberInfo getAcceptorCertifiedMemberInfo();
    CertifiedMemberInfo getRequestorCertifiedMemberInfo();
    List<PartyMemberInfo> getAcceptorPartyInfo();
    List<PartyMemberInfo> getRequestorPartyInfo();

    LocalDateTime getUpdatedAt();

    Long getAcceptorId();
    Long getRequestorId();
    UUID getBoardUUID();
}
