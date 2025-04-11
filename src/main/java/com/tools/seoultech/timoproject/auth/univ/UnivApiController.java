package com.tools.seoultech.timoproject.auth.univ;

import com.tools.seoultech.timoproject.global.APIErrorResponse;
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
        univApiFacade.certify(univRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIErrorResponse.of(true, ErrorCode.OK));
    }
    @PostMapping("/verify")
    public ResponseEntity<APIErrorResponse> verify(@RequestBody UnivRequestDTO univRequestDto, Integer code) throws Exception{
        univApiFacade.verify(univRequestDto, code);
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
