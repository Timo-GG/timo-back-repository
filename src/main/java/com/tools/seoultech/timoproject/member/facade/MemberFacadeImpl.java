package com.tools.seoultech.timoproject.member.facade;

import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.member.dto.UpdateMemberInfoRequest;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.riot.service.BasicAPIService;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
import com.tools.seoultech.timoproject.version2.memberAccount.dto.MemberAccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberFacadeImpl implements MemberFacade {

    private final MemberService memberService;
    private final BasicAPIService riotService;

    @Override
    public MemberAccountDto getMemberInfo(Long memberId) {
        MemberAccount member = memberService.getById(memberId);
        return MemberAccountDto.from(member);
    }

    @Override
    public AccountDto.Response verifyPlayer(AccountDto.Request request) {
        try {
            return riotService.findUserAccount(request);
        } catch (Exception e) {
            log.error("Failed to verify player: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to verify player", e);
        }
    }

    @Override
    public boolean checkUsername(String nickname) {
        return memberService.checkUsername(nickname);
    }

    @Override
    public MemberAccountDto updateAccountInfo(Long memberId, UpdateMemberInfoRequest request) {
        memberService.updateAccountInfo(memberId, request);
        return null;
    }


    @Override
    public MemberAccountDto getMemberProfile(Long memberId) {
        MemberAccount member = memberService.getById(memberId);
        return MemberAccountDto.from(member);
    }
}

