package com.tools.seoultech.timoproject.matching.domain.myPage.dto;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ScrimMapCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingStatus;
import lombok.Builder;

import java.util.List;
import java.util.UUID;


public class MatchingDTO {
    // NOTE: 시스템 매칭 로직 DTO.
    public record RequestDuo(
            UUID boardUUID,
            Long requestorId,
            UserInfo userInfo
    ) implements Request{}

    public record RequestScrim(
            UUID boardUUID,
            Long requestorId,
            List<PartyMemberInfo> partyInfo
    ) implements Request{}

    @Builder
    public record ResponseDuo(
            UUID myPageUUID,
            DuoMapCode mapCode,
            MatchingCategory matchingCategory,
            MatchingStatus matchingStatus,
            WrappedDuoData acceptor,
            WrappedDuoData requestor
    ) implements Response{}

    @Builder
    public record ResponseScrim(
            UUID myPageUUID,
            Integer headCount,
            ScrimMapCode mapCode,
            MatchingCategory matchingCategory,
            MatchingStatus matchingStatus,
            WrappedScrimData acceptor,
            WrappedScrimData requestor
    ) implements Response{}

    @Builder
    public record WrappedDuoData(
            CompactMemberInfo memberInfo,
            UserInfo userInfo
    ) implements Response{}

    @Builder
    public record  WrappedScrimData(
            CompactMemberInfo memberInfo,
            List<PartyMemberInfo> partyInfo
    ) implements Response{}

    public interface Request extends MatchingDtoInterface {}
    public interface Response extends MatchingDtoInterface {}
    interface MatchingDtoInterface {}
}
