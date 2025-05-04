package com.tools.seoultech.timoproject.member.dto;

import com.tools.seoultech.timoproject.member.domain.Member;
import lombok.Builder;

@Builder
public record MemberProfileDto(
        Long id,
        String nickname,
        String email,
        String playerName,
        String playerTag,
        Integer profileImageId
) {

    public static MemberProfileDto from(Member member){
        if (member == null) {
            return null;
        }
        return MemberProfileDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .playerName(member.getPlayerName())
                .playerTag(member.getPlayerTag())
                .profileImageId(member.getProfileImageId())
                .build();
    }
}
