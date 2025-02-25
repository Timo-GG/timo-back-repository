package com.tools.seoultech.timoproject.auth.dto;

import com.tools.seoultech.timoproject.member.domain.OAuthProvider;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
}