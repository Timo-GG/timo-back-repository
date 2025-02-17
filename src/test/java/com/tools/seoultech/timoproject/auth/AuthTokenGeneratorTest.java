package com.tools.seoultech.timoproject.auth;

import com.tools.seoultech.timoproject.auth.dto.AuthTokens;
import com.tools.seoultech.timoproject.auth.service.AuthTokenGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthTokensGeneratorTest {

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Test
    @DisplayName("JWT 토큰 생성 성공")
    void testGenerate() {
        // given
        Long memberId = 0L;

        // when
        AuthTokens authTokens = authTokenGenerator.generate(memberId);

        // then
        assertThat(authTokens.getGrantType()).isEqualTo("Bearer");
        assertThat(authTokens.getAccessToken()).isNotBlank();
        assertThat(authTokens.getRefreshToken()).isNotBlank();
        assertThat(authTokens.getExpiresIn()).isNotNull();
    }

    @Test
    @DisplayName("JWT 토큰 검증 성공")
    void testExtractSubject() {
        // given
        Long memberId = 0L;
        AuthTokens authTokens = authTokenGenerator.generate(memberId);
        String accessToken = authTokens.getAccessToken();

        // when
        Long extractedMemberId = authTokenGenerator.extractMemberId(accessToken);

        // then
        assertThat(extractedMemberId).isEqualTo(memberId);
    }
}