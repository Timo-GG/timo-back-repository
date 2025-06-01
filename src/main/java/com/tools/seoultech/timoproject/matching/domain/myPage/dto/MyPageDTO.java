package com.tools.seoultech.timoproject.matching.domain.myPage.dto;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;
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
    public record ResponseDuoPage(
            Long mypageId,
            Long memberId,
            DuoMapCode mapCode,
            PlayPosition myPosition,
            CertifiedMemberInfo memberInfo,
            MatchingCategory matchingCategory,
            MatchingStatus matchingStatus
    ) implements Response{}

    @Builder
    public record ResponseScrimPage(
            Long mypageId,
            Long memebrId,
            ScrimMapCode mapCode,
            PlayPosition myPosition,
            CertifiedMemberInfo memberInfo,
            MatchingCategory matchingCategory,
            MatchingStatus matchingStatus
    ) implements Response{}

    public interface Request extends BaseMyPageDtoInterface{}
    public interface Response extends BaseMyPageDtoInterface{}
    interface BaseMyPageDtoInterface{}
}
