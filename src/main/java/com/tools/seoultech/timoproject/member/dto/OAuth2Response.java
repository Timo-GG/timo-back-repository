package com.tools.seoultech.timoproject.member.dto;

public interface OAuth2Response {

    // 제공자 (e.g. Naver, Google, Riot)
    String getProvider();

    // 제공자에서 발급해주는 번호
    String getProviderId();

    // 이메일
    String getEmail();

    // 사용자 실명
    String getName();
}