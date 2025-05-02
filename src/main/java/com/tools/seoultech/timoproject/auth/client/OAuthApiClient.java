package com.tools.seoultech.timoproject.auth.client;

import com.tools.seoultech.timoproject.auth.dto.OAuthInfoResponse;
import com.tools.seoultech.timoproject.auth.dto.OAuthLoginParams;
import com.tools.seoultech.timoproject.memberAccount.domain.OAuthProvider;

public interface OAuthApiClient {

    /**  Client 타입 반환 */
    OAuthProvider oAuthProvider();

    /**  Authorization Code를 기반으로 인증 API를 요청해서 Access Token 획득 */
    String requestAccessToken(OAuthLoginParams params);

    /** Access Token 을 기반으로 Email, Nickname 등이 포함된 프로필 정보를 획득 */
    OAuthInfoResponse requestOauthInfo(String accessToken);
}