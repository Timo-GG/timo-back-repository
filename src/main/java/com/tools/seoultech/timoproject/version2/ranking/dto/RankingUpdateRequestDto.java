package com.tools.seoultech.timoproject.version2.ranking.dto;

import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.Gender;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition;

public record RankingUpdateRequestDto(
        String mbti,
        PlayPosition position,
        Gender gender,
        String memo
) {}
