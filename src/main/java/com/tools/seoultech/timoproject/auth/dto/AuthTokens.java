package com.tools.seoultech.timoproject.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class AuthTokens {

    private final String accessToken;
    @JsonIgnore
    private String refreshToken;
    private final String grantType;
    private final Long expiresIn;

    public static AuthTokens of(String accessToken, String refreshToken, String grantType, Long expiresIn) {
        return new AuthTokens(accessToken, refreshToken, grantType, expiresIn);
    }

    public static AuthTokens of(String accessToken, String grantType, Long expiresIn) {
        return new AuthTokens(accessToken, grantType, expiresIn);
    }

}