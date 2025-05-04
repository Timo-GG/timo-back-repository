package com.tools.seoultech.timoproject.auth.dto;

import com.tools.seoultech.timoproject.memberAccount.domain.OAuthProvider;

public interface OAuthInfoResponse {
    String getEmail();
    OAuthProvider getOAuthProvider();
}