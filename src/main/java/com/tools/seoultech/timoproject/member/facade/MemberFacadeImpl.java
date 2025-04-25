package com.tools.seoultech.timoproject.member.facade;

import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.member.dto.UpdateMemberInfoRequest;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;
import com.tools.seoultech.timoproject.riot.facade.RiotFacade;
import com.tools.seoultech.timoproject.riot.service.BasicAPIService;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
import com.tools.seoultech.timoproject.version2.memberAccount.dto.MemberAccountDto;
import com.tools.seoultech.timoproject.version2.ranking.dto.RankingCreateRequestDto;
import com.tools.seoultech.timoproject.version2.ranking.facade.RankingFacade;
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
    public MemberAccountDto verifyPlayer(Long memberId, AccountDto.Request request) {
        AccountDto.Response response = riotService.findUserAccount(request);
        MemberAccount memberAccount = memberService.updateRiotAccount(
            memberId, response.getPuuid(), response.getGameName(), response.getTagLine());
        return MemberAccountDto.from(memberAccount);
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

    @Override
    public MemberAccountDto updateUsername(Long memberId, String username) {
        MemberAccount memberAccount = memberService.updateUsername(memberId, username);
        return MemberAccountDto.from(memberAccount);
    }

    @Override
    public MemberAccountDto updateUniv(Long memberId, UnivRequestDTO univ) {
        MemberAccount memberAccount = memberService.updateUniv(memberId, univ);
        return MemberAccountDto.from(memberAccount);
    }

    @Override
    public MemberAccountDto resetRiotAccount(Long memberId) {
        MemberAccount memberAccount = memberService.updateRiotAccount(memberId, null, null, null);
        return MemberAccountDto.from(memberAccount);
    }

}