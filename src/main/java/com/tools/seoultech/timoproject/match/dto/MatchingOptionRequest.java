package com.tools.seoultech.timoproject.match.dto;

public record MatchingOptionRequest(
        UserInfoRequest userInfo,
        DuoInfoRequest duoInfo
) {
}
