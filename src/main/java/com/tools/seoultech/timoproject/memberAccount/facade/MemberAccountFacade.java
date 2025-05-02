package com.tools.seoultech.timoproject.memberAccount.facade;

import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
import com.tools.seoultech.timoproject.memberAccount.dto.AccountDto;
import com.tools.seoultech.timoproject.memberAccount.dto.UpdateMemberInfoRequest;
import com.tools.seoultech.timoproject.memberAccount.dto.MemberAccountDto;

public interface MemberAccountFacade {

    MemberAccountDto getMemberInfo(Long memberId);

    MemberAccountDto verifyPlayer(Long memberId, AccountDto.Request request);

    boolean checkUsername(String username);

    MemberAccountDto updateAccountInfo(Long memberId, UpdateMemberInfoRequest request);

    MemberAccountDto getMemberProfile(Long memberId);

    MemberAccountDto updateUsername(Long memberId, String username);

    MemberAccountDto updateUniv(Long memberId, UnivRequestDTO univ);

    MemberAccountDto resetRiotAccount(Long memberId);
}
