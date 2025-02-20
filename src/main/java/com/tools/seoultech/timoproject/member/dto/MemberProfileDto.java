package com.tools.seoultech.timoproject.member.dto;

import com.tools.seoultech.timoproject.member.domain.Member;
import lombok.Builder;

@Builder
public record MemberProfileDto(
        Long id,
        String username,
        String nickname,
        String email,
        String playerName,
        String playerTag
) {

    public static MemberProfileDto from(Member member){
        if (member == null) {
            return null;
        }
        return MemberProfileDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .playerName(member.getPlayerName())
                .playerTag(member.getPlayerTag())
                .build();
    }
}
