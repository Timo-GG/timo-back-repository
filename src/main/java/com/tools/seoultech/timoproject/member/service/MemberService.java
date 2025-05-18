package com.tools.seoultech.timoproject.member.service;


import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.dto.UpdateMemberInfoRequest;

public interface MemberService {

    Member getById(Long memberId);

    boolean checkUsername(String nickname);

    String randomCreateUsername();

    Integer randomCreateProfileImageId();

    Member updateAccountInfo(Long memberId, UpdateMemberInfoRequest request);

    Member updateRiotAccount(Long memberId, String puuid, String playerName, String playerTag, String profileIconUrl);

    Member updateUsername(Long memberId, String username);

    Member updateUniv(Long memberId, UnivRequestDTO univ);


}
