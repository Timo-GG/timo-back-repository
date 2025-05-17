package com.tools.seoultech.timoproject.matching.domain.board.entity.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScrimMapCode {
    RIFT("Summoner's Rift"),
    ABYSS("Howling Abyss"),
    ARENA("Arena");

    private final String name;
}
