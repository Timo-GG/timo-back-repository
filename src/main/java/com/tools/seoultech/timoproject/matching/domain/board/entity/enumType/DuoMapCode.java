package com.tools.seoultech.timoproject.matching.domain.board.entity.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DuoMapCode {
    RANK("랭크"),
    NORMAL("일반"),
    HOWLING_ABYSS("칼바람");

    private final String name;
}
