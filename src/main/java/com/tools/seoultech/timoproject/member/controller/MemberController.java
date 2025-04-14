package com.tools.seoultech.timoproject.member.controller;

import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.member.dto.MemberInfoResponse;
import com.tools.seoultech.timoproject.member.dto.MemberProfileDto;
import com.tools.seoultech.timoproject.member.dto.UpdateMemberInfoRequest;
import com.tools.seoultech.timoproject.member.facade.MemberFacade;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
import com.tools.seoultech.timoproject.version2.memberAccount.dto.MemberAccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberFacade memberFacade;

    @GetMapping
    public ResponseEntity<List<Member>> findAll() {
        return ResponseEntity.ok(memberRepository.findAll());
    }

    @GetMapping("/me")
    public ResponseEntity<APIDataResponse<MemberAccountDto>> getMember(@CurrentMemberId Long memberId) {

        MemberAccountDto memberInfo = memberFacade.getMemberInfo(memberId);

        return ResponseEntity.ok(APIDataResponse.of(memberInfo));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<APIDataResponse<MemberAccountDto>> findById(@PathVariable Long memberId) {
        MemberAccountDto memberProfile = memberFacade.getMemberProfile(memberId);
        return ResponseEntity.ok(APIDataResponse.of(memberProfile));
    }

    @PutMapping("/me/info")
    public ResponseEntity<APIDataResponse<MemberAccountDto>> updateAdditionalInfo(
            @CurrentMemberId Long memberId,
            @RequestBody UpdateMemberInfoRequest request
    ) {
        MemberAccountDto updatedInfo = memberFacade.updateAccountInfo(memberId, request);

        return ResponseEntity.ok(APIDataResponse.of(updatedInfo));
    }

    @GetMapping("/player/verify")
    public ResponseEntity<APIDataResponse<?>> verifyPlayer(
            AccountDto.Request request
    ) {
        AccountDto.Response response = memberFacade.verifyPlayer(request);

        return ResponseEntity.ok(APIDataResponse.of(response));
    }

    @GetMapping("/nickname/check")
    public ResponseEntity<APIDataResponse<?>> checkUsername(@RequestParam String username) {
        if(memberFacade.checkUsername(username)) {
            return ResponseEntity.badRequest().body(APIDataResponse.of("사용 중인 닉네임입니다."));
        } else {
            return ResponseEntity.ok(APIDataResponse.of("사용 가능한 닉네임입니다."));
        }
    }

}
