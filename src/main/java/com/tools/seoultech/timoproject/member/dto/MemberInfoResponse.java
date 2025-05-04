package com.tools.seoultech.timoproject.member.dto;

import com.tools.seoultech.timoproject.member.domain.Member;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MemberInfoResponse(MemberProfileDto memberProfile, long postCount, long commentCount, long ratingCount, BigDecimal ratingAverage) {
    public static MemberInfoResponse of(Member member, long postCount, long commentCount, long ratingCount, BigDecimal ratingAverage) {
        return MemberInfoResponse.builder()
                .memberProfile(MemberProfileDto.from(member))
                .postCount(postCount)
                .commentCount(commentCount)
                .ratingCount(ratingCount)
                .ratingAverage(ratingAverage)
                .build();
    }
}
