package com.tools.seoultech.timoproject.matching.domain.board.entity.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScrimMapCode {
    RIFT("Summoner's Rift", "소환사 협곡"),
    ABYSS("Howling Abyss", "칼바람 나락");

    private final String name;
    private final String description;
}

