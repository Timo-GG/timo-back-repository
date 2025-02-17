package com.tools.seoultech.timoproject.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverTokens {

    /**
     * Authorization Code 를 기반으로 타플랫폼 Access Token 을 받아오기
     * -> https://nid.naver.com/oauth2.0/token 요청 결과
     */

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private String expiresIn;

}
