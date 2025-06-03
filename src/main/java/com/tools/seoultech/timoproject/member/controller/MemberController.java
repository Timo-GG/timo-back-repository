package com.tools.seoultech.timoproject.member.controller;

import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.member.dto.UpdateMemberInfoRequest;
import com.tools.seoultech.timoproject.member.facade.MemberFacade;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import com.tools.seoultech.timoproject.member.dto.MemberDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/members")
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final MemberFacade memberFacade;

    @GetMapping("/me")
    public ResponseEntity<APIDataResponse<MemberDto>> getMember(@CurrentMemberId Long memberId) {

        MemberDto memberInfo = memberFacade.getMemberInfo(memberId);

        return ResponseEntity.ok(APIDataResponse.of(memberInfo));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<APIDataResponse<MemberDto>> findById(@PathVariable Long memberId) {
        MemberDto memberProfile = memberFacade.getMemberProfile(memberId);
        return ResponseEntity.ok(APIDataResponse.of(memberProfile));
    }

    @PutMapping("/me/info")
    public ResponseEntity<APIDataResponse<MemberDto>> updateAdditionalInfo(
            @CurrentMemberId Long memberId,
            @RequestBody UpdateMemberInfoRequest request
    ) {
        MemberDto updatedInfo = memberFacade.updateAccountInfo(memberId, request);

        return ResponseEntity.ok(APIDataResponse.of(updatedInfo));
    }

    @PostMapping("/player/verify")
    @Operation(summary = "라이엇 계정 인증", description = "게임 닉네임과 태그를 입력받아 라이엇 계정 인증을 수행합니다.")
    public ResponseEntity<APIDataResponse<?>> verifyPlayer(
            @CurrentMemberId Long memberId,
            @RequestBody AccountDto.Request request

    ) {
        MemberDto dto = memberFacade.verifyPlayer(memberId, request);
        return ResponseEntity.ok(APIDataResponse.of(dto));
    }

    @PutMapping("/username")
    public ResponseEntity<APIDataResponse<?>> updateUsername(
            @CurrentMemberId Long memberId,
            @RequestBody String username
    ) {
        MemberDto dto = memberFacade.updateUsername(memberId, username);

        return ResponseEntity.ok(APIDataResponse.of(dto));
    }

    @PostMapping("/riot/reset")
    public ResponseEntity<APIDataResponse<?>> resetRiotAccount(
            @CurrentMemberId Long memberId
    ) {
        MemberDto dto = memberFacade.resetRiotAccount(memberId);

        return ResponseEntity.ok(APIDataResponse.of(dto));
    }

    @PutMapping("/univ")
    public ResponseEntity<APIDataResponse<?>> updateUniv(
            @CurrentMemberId Long memberId,
            @RequestBody UnivRequestDTO univ
            ) {
        MemberDto dto = memberFacade.updateUniv(memberId, univ);

        return ResponseEntity.ok(APIDataResponse.of(dto));
    }

    @PutMapping("/userAgreement")
    public ResponseEntity<Void> updateUserAgreement(@CurrentMemberId Long memberId){
        memberFacade.updateUserAgreement(memberId);
        return ResponseEntity.ok().build();
    }
}
