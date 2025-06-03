package com.tools.seoultech.timoproject.auth.univ;

import com.tools.seoultech.timoproject.global.APIErrorResponse;
import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
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
    public ResponseEntity<APIErrorResponse> checkUniv(@Valid @RequestBody UnivRequestDTO univRequestDto) throws Exception {
        univApiFacade.checkUniv(univRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIErrorResponse.of(true, ErrorCode.OK));
    }
    @PostMapping("/request")
    public ResponseEntity<APIErrorResponse> certify(@Valid @RequestBody UnivRequestDTO univRequestDto) throws Exception {
        Boolean result = univApiFacade.certify(univRequestDto);
        APIErrorResponse response = result ? APIErrorResponse.of(result, ErrorCode.OK) : APIErrorResponse.of(result, ErrorCode.FAILED_UNIV_CERTIFY);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    @PostMapping("/verify")
    public ResponseEntity<APIErrorResponse> verify(@RequestBody UnivRequestDTO univRequestDto, Integer code) throws Exception{
        Boolean result = univApiFacade.verify(univRequestDto, code);
        APIErrorResponse response = result ? APIErrorResponse.of(result, ErrorCode.OK) : APIErrorResponse.of(result, ErrorCode.FAILED_UNIV_CERTIFY);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIErrorResponse.of(true, ErrorCode.OK));
    }

    @PostMapping("/getVerifiedUser")
    public ResponseEntity<APIDataResponse> getVerifiedUser() throws Exception {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.of(univApiFacade.getVerifiedUserList())
                );
    }
    @PostMapping("/checkStatus")
    public ResponseEntity<APIDataResponse> checkStatus(@RequestBody UnivRequestDTO univRequestDto) throws Exception {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIDataResponse.of(univApiFacade.checkStatus(univRequestDto))
                );
    }

    @DeleteMapping("/delete/me")
    public ResponseEntity<Void> deleteUniv(@CurrentMemberId Long memberId) throws Exception{
        univApiFacade.deleteCertifiedUniv(memberId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(value = {IOException.class})
    public ResponseEntity<APIErrorResponse> handleException(IOException e) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIErrorResponse.of(
                                false,
                                ErrorCode.INVALID_INPUT_VALUE,
                                e.getMessage()
                                )
                );
    }

}
