package com.tools.seoultech.timoproject.member.service;


import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
import com.tools.seoultech.timoproject.member.dto.UpdateMemberInfoRequest;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;

public interface MemberService {

    MemberAccount getById(Long memberId);

    boolean checkUsername(String nickname);

    String randomCreateUsername();

    Integer randomCreateProfileImageId();

    MemberAccount updateAccountInfo(Long memberId, UpdateMemberInfoRequest request);

    MemberAccount updateRiotAccount(Long memberId, String puuid, String playerName, String playerTag);

    MemberAccount updateUsername(Long memberId, String username);

    MemberAccount updateUniv(Long memberId, UnivRequestDTO univ);
}
