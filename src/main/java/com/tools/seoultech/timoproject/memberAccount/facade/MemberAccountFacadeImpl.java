package com.tools.seoultech.timoproject.memberAccount.facade;

import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
import com.tools.seoultech.timoproject.memberAccount.dto.AccountDto;
import com.tools.seoultech.timoproject.memberAccount.dto.UpdateMemberInfoRequest;
import com.tools.seoultech.timoproject.memberAccount.service.MemberAccountService;
import com.tools.seoultech.timoproject.riot.service.BasicAPIService;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
import com.tools.seoultech.timoproject.memberAccount.dto.MemberAccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberAccountFacadeImpl implements MemberAccountFacade {

    private final MemberAccountService memberAccountService;
    private final BasicAPIService riotService;
    @Override
    public MemberAccountDto getMemberInfo(Long memberId) {
        MemberAccount member = memberAccountService.getById(memberId);
        return MemberAccountDto.from(member);
    }

    @Override
    public MemberAccountDto verifyPlayer(Long memberId, AccountDto.Request request) {
        AccountDto.Response response = riotService.findUserAccount(request);
        MemberAccount memberAccount = memberAccountService.updateRiotAccount(
            memberId, response.getPuuid(), response.getGameName(), response.getTagLine());
        return MemberAccountDto.from(memberAccount);
    }

    @Override
    public boolean checkUsername(String nickname) {
        return memberAccountService.checkUsername(nickname);
    }

    @Override
    public MemberAccountDto updateAccountInfo(Long memberId, UpdateMemberInfoRequest request) {
        memberAccountService.updateAccountInfo(memberId, request);
        return null;
    }


    @Override
    public MemberAccountDto getMemberProfile(Long memberId) {
        MemberAccount member = memberAccountService.getById(memberId);
        return MemberAccountDto.from(member);
    }

    @Override
    public MemberAccountDto updateUsername(Long memberId, String username) {
        MemberAccount memberAccount = memberAccountService.updateUsername(memberId, username);
        return MemberAccountDto.from(memberAccount);
    }

    @Override
    public MemberAccountDto updateUniv(Long memberId, UnivRequestDTO univ) {
        MemberAccount memberAccount = memberAccountService.updateUniv(memberId, univ);
        return MemberAccountDto.from(memberAccount);
    }

    @Override
    public MemberAccountDto resetRiotAccount(Long memberId) {
        MemberAccount memberAccount = memberAccountService.updateRiotAccount(memberId, null, null, null);
        return MemberAccountDto.from(memberAccount);
    }

}