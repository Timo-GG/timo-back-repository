package com.tools.seoultech.timoproject.auth.client;

import com.tools.seoultech.timoproject.auth.dto.*;
import com.tools.seoultech.timoproject.member.domain.OAuthProvider;
import com.tools.seoultech.timoproject.riot.service.RiotAPIService;
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
public class RiotApiClient implements OAuthApiClient {

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.riot.url.auth}")
    private String authUrl;

    @Value("${oauth.riot.url.api}")
    private String apiUrl;

    @Value("${oauth.riot.client-id}")
    private String clientId;

    @Value("${oauth.riot.secret}")
    private String clientSecret;

    @Value("${oauth.riot.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;
    private final RiotAPIService riotAPIService;


    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.RIOT;
    }

    /** 라이엇 로그인 요청 API */
    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        String url = authUrl + "/token";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Basic Auth 헤더 추가 (Riot은 Basic Auth 사용)
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
        httpHeaders.set("Authorization", "Basic " + encodedAuth);

        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("redirect_uri", redirectUri);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        RiotTokens response = restTemplate.postForObject(url, request, RiotTokens.class);

        log.info("📌 Riot AccessToken 응답: {}", response);

        assert response != null;
        return response.getAccessToken();
    }

    /** 라이엇 회원 프로필 조회 API */
    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        // 1. 기본 유저 정보 조회
        RiotInfoResponse userInfo = getUserInfo(accessToken);

        // 2. 계정 정보 조회 추가
        try {
            RiotAccountInfo accountInfo = getRiotAccountInfo(accessToken);
            if (accountInfo != null) {
                String profileIconUrl = riotAPIService.getProfileIconUrlByPuuid(accountInfo.getPuuid());
                RiotAccountInfo enrichedAccountInfo = accountInfo.withProfileIconUrl(profileIconUrl);

                userInfo = userInfo.withAccountInfo(enrichedAccountInfo);
                log.info("✅ RSO 계정 정보 조회 성공: {}#{}",
                        accountInfo.getGameName(), accountInfo.getTagLine());
            }
        } catch (Exception e) {
            log.error("❌ RSO 계정 정보 조회 실패: {}", e.getMessage());
            // 계정 정보 없이도 로그인은 가능하도록 처리
        }

        return userInfo;
    }

    private RiotInfoResponse getUserInfo(String accessToken) {
        String url = authUrl + "/userinfo";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);

        RiotInfoResponse response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                RiotInfoResponse.class
        ).getBody();

        log.info("📌 Riot 유저 정보: {}", response);
        if (response != null) {
            log.info("✅ Riot 이메일: {}", response.getEmail());
        } else {
            log.warn("❗ Riot 사용자 정보가 null입니다.");
        }

        return response;
    }

    private RiotAccountInfo getRiotAccountInfo(String accessToken) {
        try {
            String url = "https://americas.api.riotgames.com/riot/account/v1/accounts/me";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + accessToken);
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> request = new HttpEntity<>(httpHeaders);

            RiotAccountInfo response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    RiotAccountInfo.class
            ).getBody();

            log.info("📌 RSO 계정 정보: {}", response);
            return response;

        } catch (Exception e) {
            log.error("❌ RSO 계정 정보 조회 실패: {}", e.getMessage());
            return null;
        }
    }
}