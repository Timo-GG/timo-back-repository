package com.tools.seoultech.timoproject.matching.domain.myPage.dto;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingStatus;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public abstract class MyPageDTO {
    // Note: 조회용
    public record RequestSearch(
            UUID myPageUUID,
            MatchingCategory matchingCategory,
            Boolean isRequestor,
            MatchingStatus status
    ){}

    @Builder
    public record ResponseMyPage(
            Integer sizeOfDuo,
            Integer sizeOfScrim,
            List<MatchingDTO.ResponseDuo> duoList,
            List<MatchingDTO.ResponseScrim> scrimList
    ){}
}
