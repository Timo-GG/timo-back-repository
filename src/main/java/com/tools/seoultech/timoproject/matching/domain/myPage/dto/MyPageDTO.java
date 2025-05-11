package com.tools.seoultech.timoproject.matching.domain.myPage.dto;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingStatus;
import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

// Note: dto별로 존재하는 Request, Response를 어노테이션으로 메타데이터를 만들면 어떨까?
public abstract class MyPageDTO {
    // Note: 1. 유저 필터링 기반 조회용.
    @Builder
    public record RequestSearch(
            UUID myPageUUID,  // NOTE: 단일 조회
            MatchingCategory matchingCategory, // NOTE: 듀오•콜로세움 매칭보드
            Long memberId,
            Boolean isRequestor,  // NOTE: 보낸 요청, 받은 요청
            MatchingStatus status  // NOTE: 매칭 분기 상태
    ){}

    // Note: 2. 검색 결과 Response
    public record ResponseMyPage(
            UUID myPageUUID,
            MatchingCategory matchingCategory,
            @Schema(oneOf = {BoardDTO.ResponseDuo.class, BoardDTO.ResponseColosseum.class}) BoardDTO.Response acceptorBoard,
            @Schema(oneOf = {RedisUser.Duo.class, RedisUser.Colosseum.class}) UserDTO.Response requestor,
            MatchingStatus status
    ){}
}
