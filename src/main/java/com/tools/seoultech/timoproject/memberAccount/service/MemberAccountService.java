package com.tools.seoultech.timoproject.memberAccount.service;


import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
import com.tools.seoultech.timoproject.memberAccount.dto.UpdateMemberInfoRequest;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;

public interface MemberAccountService {

    MemberAccount getById(Long memberId);

    boolean checkUsername(String nickname);

    String randomCreateUsername();

    Integer randomCreateProfileImageId();

    MemberAccount updateAccountInfo(Long memberId, UpdateMemberInfoRequest request);

    MemberAccount updateRiotAccount(Long memberId, String puuid, String playerName, String playerTag, String profileIconUrl);

    MemberAccount updateUsername(Long memberId, String username);

    MemberAccount updateUniv(Long memberId, UnivRequestDTO univ);


}
