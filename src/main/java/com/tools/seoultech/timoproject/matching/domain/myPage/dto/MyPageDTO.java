package com.tools.seoultech.timoproject.matching.domain.myPage.dto;

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
    public record ResponseDuoPage(
            Long mypageId,
            DuoMapCode mapCode,
            MatchingCategory matchingCategory,
            MatchingStatus matchingStatus,
            MatchingDTO.WrappedDuoData acceptor,
            MatchingDTO.WrappedDuoData requestor
    ) implements Response{}

    @Builder
    public record ResponseScrimPage(
            Long mypageId,
            ScrimMapCode mapCode,
            MatchingCategory matchingCategory,
            MatchingStatus matchingStatus,
            MatchingDTO.WrappedScrimData acceptor,
            MatchingDTO.WrappedScrimData requestor
    ) implements Response{}

    public interface Request extends BaseMyPageDtoInterface{}
    public interface Response extends BaseMyPageDtoInterface{}
    interface BaseMyPageDtoInterface{}
}
