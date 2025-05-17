package com.tools.seoultech.timoproject.matching.domain.myPage.dto;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import lombok.Builder;

import java.util.List;
import java.util.UUID;


public class MatchingDTO {
    // NOTE: 시스템 매칭 로직 DTO.
    public record RequestDuo(
            UUID boardUUID,
            Long requestorId,
            UserInfo userInfo
    ){}

    public record RequestScrim(
            UUID boardUUID,
            Long requestorId,
            List<PartyMemberInfo> partyInfo
    ){}

    @Builder
    public record ResponseDuo(
            UUID myPageUUID,
            WrappedDuoData acceptor,
            WrappedDuoData requestor
    ){}

    @Builder
    public record ResponseScrim(
            UUID myPageUUID,
            WrappedScrimData acceptor,
            WrappedScrimData requestor
    ){}

    @Builder
    public record WrappedDuoData(
            CertifiedMemberInfo memberInfo,
            UserInfo userInfo
    ){}

    @Builder
    public record  WrappedScrimData(
            CertifiedMemberInfo memberInfo,
            List<PartyMemberInfo> partyInfo
    ){}

}
