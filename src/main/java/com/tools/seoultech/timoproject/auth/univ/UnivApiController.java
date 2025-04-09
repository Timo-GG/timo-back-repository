package com.tools.seoultech.timoproject.auth.univ;

import com.tools.seoultech.timoproject.global.APIErrorResponse;
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
    @ExceptionHandler
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
