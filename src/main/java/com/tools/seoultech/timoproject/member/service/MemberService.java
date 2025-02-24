package com.tools.seoultech.timoproject.member.service;


import com.tools.seoultech.timoproject.member.domain.Member;

public interface MemberService {

    Member getById(Long memberId);

    boolean checkNickname(String nickname);

    String randomCreateNickname();
}
