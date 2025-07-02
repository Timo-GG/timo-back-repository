package com.tools.seoultech.timoproject.member.controller;

import com.tools.seoultech.timoproject.auth.dto.RiotLoginParams;
import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.member.dto.*;
import com.tools.seoultech.timoproject.member.facade.MemberFacade;
import com.tools.seoultech.timoproject.member.facade.VerificationSyncFacade;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    private final VerificationSyncFacade verificationSyncFacade;

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

    @DeleteMapping("/userAgreement/soft")
    public ResponseEntity<Void> softDeleteUserAgreement(@CurrentMemberId Long memberId){
        memberFacade.softDeleteUserAgreement(memberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/userAgreement/hard")
    public ResponseEntity<Void> hardDeleteUserAgreement(@CurrentMemberId Long memberId){
        memberFacade.hardDeleteUserAgreement(memberId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/riot/link")
    public ResponseEntity<APIDataResponse<String>> linkRiotAccount(
            @RequestBody RiotLoginParams params,
            @CurrentMemberId Long memberId) {

        String result = memberFacade.linkRiotAccount(memberId, params);
        return ResponseEntity.ok(APIDataResponse.of(result));
    }

    @Operation(summary = "이메일 알림 설정 업데이트")
    @PutMapping("/notification-email")
    public ResponseEntity<Void> updateNotificationEmail(
            @CurrentMemberId Long memberId,
            @RequestBody @Valid NotificationEmailRequest request) {

        memberFacade.updateNotificationEmail(memberId, request.notificationEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이메일 알림 설정 조회")
    @GetMapping("/notification-email")
    public ResponseEntity<APIDataResponse<NotificationEmailResponse>> getNotificationEmail(
            @CurrentMemberId Long memberId) {

        NotificationEmailResponse response = memberFacade.getNotificationEmailSettings(memberId);
        return ResponseEntity.ok(APIDataResponse.of(response));
    }

    @Operation(summary = "인증 타입 업데이트", description = "사용자의 인증 타입을 모든 시스템에서 업데이트합니다.")
    @PostMapping("/verification-type")
    public ResponseEntity<?> updateVerificationType(
            @CurrentMemberId Long memberId,
            @RequestParam String verificationType) {

        verificationSyncFacade.syncVerificationTypeAcrossAllSystems(memberId, verificationType);

        return ResponseEntity.ok(APIDataResponse.of("모든 시스템의 인증 타입이 동기화되었습니다."));
    }
}
