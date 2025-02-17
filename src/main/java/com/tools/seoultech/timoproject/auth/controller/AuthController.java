package com.tools.seoultech.timoproject.auth.controller;

import com.tools.seoultech.timoproject.auth.dto.AuthTokens;
import com.tools.seoultech.timoproject.auth.dto.NaverLoginParams;
import com.tools.seoultech.timoproject.auth.jwt.JwtTokenProvider;
import com.tools.seoultech.timoproject.auth.service.AuthTokenGenerator;
import com.tools.seoultech.timoproject.auth.service.OAuthLoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {


    private final OAuthLoginService oAuthLoginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthTokenGenerator authTokenGenerator;

    @PostMapping("/naver")
    public ResponseEntity<AuthTokens> loginNaver(@RequestBody NaverLoginParams params, HttpServletResponse response) {
        AuthTokens tokens = oAuthLoginService.login(params);

        // Refresh Token을 HTTPOnly 쿠키로 설정
        Cookie refreshTokenCookie = createRefreshTokenCookie(tokens.getRefreshToken());
        response.addCookie(refreshTokenCookie);

        // 응답 본문에는 accessToken만 포함
        AuthTokens responseTokens = AuthTokens.of(tokens.getAccessToken(), tokens.getGrantType(), tokens.getExpiresIn());
        return ResponseEntity.ok(responseTokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthTokens> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken,
                                                   HttpServletResponse response) {
        // refresh token이 없으면 인증 실패 처리
        if (refreshToken == null || refreshToken.isEmpty()) {
            log.warn("Refresh token is missing.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // refresh token 검증
        JwtTokenProvider.TokenValidationResult validationResult = jwtTokenProvider.validate(refreshToken);
        if (validationResult != JwtTokenProvider.TokenValidationResult.VALID) {
            log.warn("Refresh token validation failed: {}", validationResult);
            HttpStatus status = validationResult == JwtTokenProvider.TokenValidationResult.EXPIRED
                    ? HttpStatus.UNAUTHORIZED : HttpStatus.FORBIDDEN;
            return ResponseEntity.status(status).build();
        }

        // refresh token에서 subject(회원 식별자) 추출
        String subject = jwtTokenProvider.extractSubject(refreshToken);
        Long memberId = Long.valueOf(subject);

        // 새 access token (및 필요 시 새로운 refresh token) 생성
        AuthTokens newTokens = authTokenGenerator.generate(memberId);

        // 새 refresh token을 HTTPOnly 쿠키에 설정 (refresh token은 응답 본문에 노출하지 않음)
        Cookie newCookie = createRefreshTokenCookie(newTokens.getRefreshToken());
        response.addCookie(newCookie);

        AuthTokens responseTokens = AuthTokens.of(newTokens.getAccessToken(), newTokens.getGrantType(), newTokens.getExpiresIn());
        return ResponseEntity.ok(responseTokens);
    }

    /**
     * Refresh token 쿠키를 생성하는 공통 메서드
     */
    private Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // HTTPS 사용 시에만 true로 설정
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7); // 예: 7일간 유효
        return cookie;
    }
}
