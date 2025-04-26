package com.tools.seoultech.timoproject.version2.ranking.dto;

import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.Gender;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "랭킹 업데이트 요청 DTO")
public record RankingUpdateRequestDto(

        @Schema(description = "MBTI", example = "INFJ")
        String mbti,

        @Schema(description = "플레이 포지션", example = "TOP")
        PlayPosition position,

        @Schema(description = "성별", example = "MALE")
        Gender gender,

        @Schema(description = "학과", example = "컴퓨터공학과")
        String department,

        @Schema(description = "한 줄 소개 메모", example = "함께 즐겜할 유저 찾아요!")
        String memo
) {}
