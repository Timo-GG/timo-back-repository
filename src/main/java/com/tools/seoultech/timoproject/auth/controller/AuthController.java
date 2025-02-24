package com.tools.seoultech.timoproject.auth.controller;

import com.tools.seoultech.timoproject.auth.dto.NaverLoginParams;
import com.tools.seoultech.timoproject.auth.facade.AuthFacade;
import com.tools.seoultech.timoproject.auth.jwt.HeaderTokenExtractor;
import com.tools.seoultech.timoproject.auth.jwt.TokenCollection;
import com.tools.seoultech.timoproject.auth.service.OAuthLoginService;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {


    private final OAuthLoginService oAuthLoginService;
    private final AuthFacade authFacade;
    private final HeaderTokenExtractor headerTokenExtractor;

    @PostMapping("/naver")
    public ResponseEntity<TokenCollection> loginNaver(@RequestBody NaverLoginParams params, HttpServletResponse response) {
        TokenCollection tokens = oAuthLoginService.login(params);

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<APIDataResponse<TokenCollection>> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        String extractRefreshToken = headerTokenExtractor.extractRefreshToken(refreshToken);
        TokenCollection tokenCollection = authFacade.newTokenInfo(extractRefreshToken);

        return ResponseEntity.ok(APIDataResponse.of(tokenCollection));
    }

    @GetMapping("/test")
    public ResponseEntity<TokenCollection> testLogin() {
        TokenCollection tokens = authFacade.testLogin();

        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/verify")
    public ResponseEntity<APIDataResponse<?>> verifyPlayer(
            AccountDto.Request request
    ) {
        AccountDto.Response response = authFacade.verifyPlayer(request);

        return ResponseEntity.ok(APIDataResponse.of(response));
    }


}
