package com.tools.seoultech.timoproject.member.facade;

import com.tools.seoultech.timoproject.auth.dto.RiotLoginParams;
import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.member.dto.UpdateMemberInfoRequest;
import com.tools.seoultech.timoproject.member.dto.MemberDto;

public interface MemberFacade {

    MemberDto getMemberInfo(Long memberId);

    MemberDto verifyPlayer(Long memberId, AccountDto.Request request);

    boolean checkUsername(String username);

    MemberDto updateAccountInfo(Long memberId, UpdateMemberInfoRequest request);

    MemberDto getMemberProfile(Long memberId);

    MemberDto updateUsername(Long memberId, String username);

    MemberDto updateUniv(Long memberId, UnivRequestDTO univ);

    MemberDto resetRiotAccount(Long memberId);

    void updateUserAgreement(Long memberId);
    void softDeleteUserAgreement(Long memberId);
    void hardDeleteUserAgreement(Long memberId);

    String linkRiotAccount(Long memberId, RiotLoginParams params);
}
