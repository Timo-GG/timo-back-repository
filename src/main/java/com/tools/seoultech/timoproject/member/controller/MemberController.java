package com.tools.seoultech.timoproject.member.controller;

import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/members")
@Tag(name = "Member", description = "Member API")
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

    @PostMapping("/player/verify")
    @Operation(summary = "라이엇 계정 인증", description = "게임 닉네임과 태그를 입력받아 라이엇 계정 인증을 수행합니다.")
    public ResponseEntity<APIDataResponse<?>> verifyPlayer(
            @CurrentMemberId Long memberId,
            @RequestBody AccountDto.Request request

    ) {
        MemberAccountDto dto = memberFacade.verifyPlayer(memberId, request);
        return ResponseEntity.ok(APIDataResponse.of(dto));
    }

    @PutMapping("/username")
    public ResponseEntity<APIDataResponse<?>> updateUsername(
            @CurrentMemberId Long memberId,
            @RequestBody String username
    ) {
        MemberAccountDto dto = memberFacade.updateUsername(memberId, username);

        return ResponseEntity.ok(APIDataResponse.of(dto));
    }

    @PostMapping("/riot/reset")
    public ResponseEntity<APIDataResponse<?>> resetRiotAccount(
            @CurrentMemberId Long memberId
    ) {
        MemberAccountDto dto = memberFacade.resetRiotAccount(memberId);

        return ResponseEntity.ok(APIDataResponse.of(dto));
    }

    @PutMapping("/univ")
    public ResponseEntity<APIDataResponse<?>> updateUniv(
            @CurrentMemberId Long memberId,
            @RequestBody UnivRequestDTO univ
            ) {
        MemberAccountDto dto = memberFacade.updateUniv(memberId, univ);

        return ResponseEntity.ok(APIDataResponse.of(dto));
    }


}
