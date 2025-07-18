package com.tools.seoultech.timoproject.member.facade;

import com.tools.seoultech.timoproject.auth.dto.RiotLoginParams;
import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.member.dto.NotificationEmailResponse;
import com.tools.seoultech.timoproject.member.dto.UpdateMemberInfoRequest;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.riot.service.RiotAPIService;
import com.tools.seoultech.timoproject.member.dto.MemberDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberFacadeImpl implements MemberFacade {

    private final MemberService memberService;
    private final RiotAPIService riotService;
    @Override
    public MemberDto getMemberInfo(Long memberId) {
        Member member = memberService.getById(memberId);
        return MemberDto.from(member);
    }

    @Override
    public MemberDto verifyPlayer(Long memberId, AccountDto.Request request) {
        AccountDto.Response response = riotService.findUserAccount(request);
        String profileUrl = riotService.getProfileIconUrlByPuuid(response.getPuuid());
        Member member = memberService.updateRiotAccount(
            memberId, response.getPuuid(), response.getGameName(), response.getTagLine(), profileUrl);
        return MemberDto.from(member);
    }

    @Override
    public boolean checkUsername(String nickname) {
        return memberService.checkUsername(nickname);
    }

    @Override
    public MemberDto updateAccountInfo(Long memberId, UpdateMemberInfoRequest request) {
        memberService.updateAccountInfo(memberId, request);
        return null;
    }


    @Override
    public MemberDto getMemberProfile(Long memberId) {
        Member member = memberService.getById(memberId);
        return MemberDto.from(member);
    }

    @Override
    public MemberDto updateUsername(Long memberId, String username) {
        Member member = memberService.updateUsername(memberId, username);
        return MemberDto.from(member);
    }

    @Override
    public MemberDto updateUniv(Long memberId, UnivRequestDTO univ) {
        Member member = memberService.updateUniv(memberId, univ);
        return MemberDto.from(member);
    }

    @Override
    public MemberDto resetRiotAccount(Long memberId) {
        Member member = memberService.updateRiotAccount(memberId, null, null, null, null);
        return MemberDto.from(member);
    }

    @Override
    @Transactional
    public void updateUserAgreement(Long memberId) {
        memberService.updateUserAgreement(memberId);;
    }

    @Override
    @Transactional
    public void softDeleteUserAgreement(Long memberId) {
        memberService.softDeleteUserAgreement(memberId);
    }

    @Override
    @Transactional
    public void hardDeleteUserAgreement(Long memberId) {
        memberService.hardDeleteUserAgreement(memberId);
    }

    @Override
    @Transactional
    public String linkRiotAccount(Long memberId, RiotLoginParams params) {
        return memberService.linkRiotAccount(memberId, params);
    }

    @Override
    public void updateNotificationEmail(Long memberId, String notificationEmail) {
        memberService.updateNotificationEmail(memberId, notificationEmail);
    }

    @Override
    public NotificationEmailResponse getNotificationEmailSettings(Long memberId) {
        return memberService.getNotificationEmailSettings(memberId);
    }
}