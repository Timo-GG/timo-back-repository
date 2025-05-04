package com.tools.seoultech.timoproject.match.dto;


import com.tools.seoultech.timoproject.member.domain.Member;
import lombok.Getter;

@Getter
public class MatchingOptionResponse {
    private Long memberId;
    private UserInfoResponse userInfo;
    private DuoInfoResponse duoInfo;

    public MatchingOptionResponse(Member member) {
        this.memberId = member.getId();
        this.userInfo = new UserInfoResponse(member.getUserInfo());
        this.duoInfo = new DuoInfoResponse(member.getDuoInfo());
    }
}
