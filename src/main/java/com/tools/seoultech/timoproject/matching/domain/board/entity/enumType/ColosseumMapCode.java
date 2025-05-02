package com.tools.seoultech.timoproject.version2.matching.domain.board.entity.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ColosseumMapCode {
    RIFT("Summoner's Rift"),
    ABYSS("Howling Abyss"),
    ARENA("Arena");

    private final String name;
}
