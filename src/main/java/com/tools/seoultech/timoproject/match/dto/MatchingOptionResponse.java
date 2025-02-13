package com.tools.seoultech.timoproject.match.dto;


import com.tools.seoultech.timoproject.member.domain.Member;

public record MatchingOptionResponse(
        Long memberId,
        UserInfoResponse userInfo,
        DuoInfoResponse duoInfo
) {

    public static MatchingOptionResponse of(Long memberId, UserInfoResponse userInfo, DuoInfoResponse duoInfo) {
        return new MatchingOptionResponse(memberId, userInfo, duoInfo);
    }

    public static MatchingOptionResponse from(Member member) {
        return new MatchingOptionResponse(
                member.getId(),
                UserInfoResponse.from(member.getUserInfo()),
                DuoInfoResponse.from(member.getDuoInfo())
        );
    }
}