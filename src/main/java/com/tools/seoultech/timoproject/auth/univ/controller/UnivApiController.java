package com.tools.seoultech.timoproject.auth.univ.controller;

import com.tools.seoultech.timoproject.auth.univ.UnivApiFacade;
import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
import com.tools.seoultech.timoproject.global.APIErrorResponse;
import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth/univ")
@RequiredArgsConstructor
public class UnivApiController {
    private final UnivApiFacade univApiFacade;

    @PostMapping("/checkUniv")
    public ResponseEntity<APIErrorResponse> checkUniv(@Valid @RequestBody UnivRequestDTO univRequestDto) {
        univApiFacade.checkUniv(univRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(APIErrorResponse.of(true, ErrorCode.OK));
    }

    @PostMapping("/request")
    public ResponseEntity<APIErrorResponse> certify(@Valid @RequestBody UnivRequestDTO univRequestDto) throws IOException {
        Boolean result = univApiFacade.certify(univRequestDto);
        APIErrorResponse response = result ? APIErrorResponse.of(true, ErrorCode.OK) : APIErrorResponse.of(false, ErrorCode.FAILED_UNIV_CERTIFY);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/verify/me")
    public ResponseEntity<APIErrorResponse> verify(@RequestBody UnivRequestDTO univRequestDto,
                                                   @RequestParam("code") Integer code,
                                                   @CurrentMemberId Long memberId) {

        Boolean result = univApiFacade.verify(univRequestDto, code, memberId);
        APIErrorResponse response = result ? APIErrorResponse.of(true, ErrorCode.OK) : APIErrorResponse.of(false, ErrorCode.FAILED_UNIV_CERTIFY);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/me")
    public ResponseEntity<Void> deleteUniv(@CurrentMemberId Long memberId) {
        univApiFacade.deleteCertifiedUniv(memberId);
        return ResponseEntity.noContent().build();
    }
}