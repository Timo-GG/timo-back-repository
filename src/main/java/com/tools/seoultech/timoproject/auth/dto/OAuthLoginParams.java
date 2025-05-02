package com.tools.seoultech.timoproject.auth.dto;

import com.tools.seoultech.timoproject.memberAccount.domain.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
    OAuthProvider oAuthProvider();
    MultiValueMap<String, String> makeBody();
}
