package com.tools.seoultech.timoproject.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigInteger;
import java.security.SecureRandom;

@Controller
@RequestMapping("/api/v1/naver")
public class NaverController {

    @Value("${oauth.naver.url.auth}")
    private String authUrl;

    @Value("${oauth.naver.url.api}")
    private String apiUrl;

    @Value("${oauth.naver.client-id}")
    private String clientId;

    @Value("${oauth.naver.secret}")
    private String clientSecret;

    @GetMapping("/auth")
    public String naverConnect() {

        // State 전용 난수 생성
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString(32);

        // Redirect URL
        String url = authUrl + "/oauth2.0/authorize?" +
                "response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fnaver%2Fcallback" +
                "&state=" + state;

        return "redirect:" + url;
    }
}
