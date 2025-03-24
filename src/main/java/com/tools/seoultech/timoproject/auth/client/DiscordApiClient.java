package com.tools.seoultech.timoproject.auth.client;

import com.tools.seoultech.timoproject.auth.dto.*;
import com.tools.seoultech.timoproject.member.domain.OAuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiscordApiClient implements OAuthApiClient {

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.discord.url.api}")
    private String apiUrl;

    @Value("${oauth.discord.redirect-uri}")
    private String redirectUri;

    @Value("${oauth.discord.scope}")
    private String scope;

    @Value("${oauth.discord.client-id}")
    private String clientId;

    @Value("${oauth.discord.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.DISCORD;
    }

    /** 디스코드 로그인 요청 API */
    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        String url = apiUrl + "/oauth2/token";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("scope", scope);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        DiscordTokens response = restTemplate.postForObject(url, request, DiscordTokens.class);

        assert response != null;
        log.info("resourceToken : " + response.getAccessToken());
        return response.getAccessToken();
    }

    /** 디스코드 회원 프로필 조회 API */
    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        String url = apiUrl + "/users/@me";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        return restTemplate.exchange(
                url,
                HttpMethod.GET, // POST -> GET으로 변경
                request,
                DiscordInfoResponse.class
        ).getBody();
    }

    /** 디스코드 전체 토큰 요청 API */
    public DiscordTokens requestToken(OAuthLoginParams params) {
        String url = apiUrl + "/oauth2/token";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("scope", scope);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        return restTemplate.postForObject(url, request, DiscordTokens.class);
    }
}

