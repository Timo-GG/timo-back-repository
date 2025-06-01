package com.tools.seoultech.timoproject.matching.domain.myPage.dto;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ScrimMapCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingStatus;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public abstract class MyPageDTO {
    // Note: 조회용
    public record RequestSearch(
            UUID myPageUUID,
            MatchingCategory matchingCategory,
            Boolean isRequestor,
            MatchingStatus status
    ) implements Request{}

    @Builder
    public record ResponseMyPage(
            Integer size,
            String filteredBy,
            List<MyPageDTO.Response> dtoList
    ) implements Response{}

    @Builder
    // NOTE: 보낸 요청, 받은 요청 보기용.
    public record ResponseDuoReviewPage(
            Long mypageId,
            Long memberId,
            DuoMapCode mapCode,
            CompactMemberInfo memberInfo,
            MatchingCategory matchingCategory,
            MatchingStatus matchingStatus
    ) implements Response{}

    @Builder
    public record ResponseScrimReviewPage(
            Long mypageId,
            Long memberId,
            ScrimMapCode mapCode,
            CompactMemberInfo memberInfo,
            MatchingCategory matchingCategory,
            MatchingStatus matchingStatus
    ) implements Response{}

    @Builder
    public record ResponseDuoPage(
            Long mypageId,
            Long acceptorId,
            Long requestorId,
            DuoMapCode mapCode,
            MatchingCategory matchingCategory,
            MatchingStatus matchingStatus,
            CompactMemberInfo acceptorMemberInfo,
            CompactMemberInfo requestorMemberInfo,
            UserInfo acceptorUserInfo,
            UserInfo requestorUserInfo
    ) implements Response{}

    @Builder
    public record ResponseScrimPage(
            Long mypageId,
            Long acceptorId,
            Long requestorId,
            Integer headCount,
            ScrimMapCode mapCode,
            MatchingCategory matchingCategory,
            MatchingStatus matchingStatus,
            CompactMemberInfo acceptorMemberInfo,
            CompactMemberInfo requestorMemberInfo,
            List<CompactMemberInfo> acceptorPartyInfo,
            List<CompactMemberInfo> requestorPartyInfo
    )implements Response{}

    public interface Request extends BaseMyPageDtoInterface{}
    public interface Response extends BaseMyPageDtoInterface{}
    interface BaseMyPageDtoInterface{}
}
