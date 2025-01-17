package com.tools.seoultech.timoproject.member.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class OAuth2Member implements OAuth2User {

    private final OAuth2Response oAuth2Response;
    private final String role;

    @Override
    public Map<String, Object> getAttributes() { // 로그인 후에 서버로부터 넘어오는 값들
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // 사용자 권한 정보
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role;
            }
        });
        return collection;
    }

    @Override
    public String getName() { // 사용자 실명(별명)
        return oAuth2Response.getName();
    }

    public String getUsername() { // 사용자 id(고유값)
        return oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
    }
}