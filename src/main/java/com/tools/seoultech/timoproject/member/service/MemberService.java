package com.tools.seoultech.timoproject.member.service;


import com.tools.seoultech.timoproject.member.domain.Member;

public interface MemberService {

    Member getById(Long memberId);

    boolean checkNickname(String nickname);

    String randomCreateNickname();

    Integer randomCreateProfileImageId();

    Member updateAdditionalInfo(Long memberId, String nickname, String playerName, String playerTag);

    Integer updateProfileImageId(Long memberId, Integer imageId);
}
