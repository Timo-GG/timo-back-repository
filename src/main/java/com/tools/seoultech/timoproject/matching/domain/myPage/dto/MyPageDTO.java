package com.tools.seoultech.timoproject.matching.domain.myPage.dto;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ScrimMapCode;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingStatus;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.ReviewStatus;
import com.tools.seoultech.timoproject.review.Review;
import lombok.Builder;

import java.time.LocalDateTime;
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
    public record ResponseDuoPage(
            Long mypageId,
            DuoMapCode mapCode,
            MatchingCategory matchingCategory,
            MatchingStatus matchingStatus,
            MyPageWrappedDuoData acceptor,
            MyPageWrappedDuoData requestor,
            Long roomId,
            Long acceptorId,
            Long requestorId,
            Review acceptorReview,
            Review requestorReview,
            ReviewStatus reviewStatus,
            LocalDateTime createdAt
    ) implements Response{}

    @Builder
    public record ResponseScrimPage(
            Long mypageId,
            ScrimMapCode mapCode,
            MatchingCategory matchingCategory,
            MatchingStatus matchingStatus,
            MyPageWrappedScrimData acceptor,
            MyPageWrappedScrimData requestor,
            Long roomId,
            Long acceptorId,
            Long requestorId,
            Review acceptorReview,
            Review requestorReview,
            ReviewStatus reviewStatus,
            LocalDateTime createdAt
    ) implements Response{}

    @Builder
    public record MyPageWrappedDuoData(
            CompactMemberInfo memberInfo,
            UserInfo userInfo,
            String univName,      // 대학명 추가
            String department     // 학과명 추가
    ) implements Response{}

    @Builder
    public record MyPageWrappedScrimData(
            CompactMemberInfo memberInfo,
            List<PartyMemberInfo> partyInfo,
            String univName,      // 대학명 추가
            String department     // 학과명 추가
    ) implements Response{}

    public interface Request extends BaseMyPageDtoInterface{}
    public interface Response extends BaseMyPageDtoInterface{}
    interface BaseMyPageDtoInterface{}
}
