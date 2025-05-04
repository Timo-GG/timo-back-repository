package com.tools.seoultech.timoproject.member.facade;

import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.member.dto.MemberInfoResponse;
import com.tools.seoultech.timoproject.member.dto.MemberProfileDto;
import com.tools.seoultech.timoproject.member.dto.UpdateMemberInfoRequest;
import com.tools.seoultech.timoproject.version2.memberAccount.dto.MemberAccountDto;

public interface MemberFacade {

    MemberAccountDto getMemberInfo(Long memberId);

    MemberAccountDto verifyPlayer(Long memberId, AccountDto.Request request);

    boolean checkUsername(String username);

    MemberAccountDto updateAccountInfo(Long memberId, UpdateMemberInfoRequest request);

    MemberAccountDto getMemberProfile(Long memberId);

    MemberAccountDto updateUsername(Long memberId, String username);

    MemberAccountDto updateUniv(Long memberId, UnivRequestDTO univ);

    MemberAccountDto resetRiotAccount(Long memberId);
}
