package com.tools.seoultech.timoproject.auth.facade;

import com.tools.seoultech.timoproject.auth.jwt.JwtProvider;
import com.tools.seoultech.timoproject.auth.jwt.JwtResolver;
import com.tools.seoultech.timoproject.auth.jwt.TokenCollection;
import com.tools.seoultech.timoproject.auth.jwt.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacadeImpl implements AuthFacade {

    private final JwtResolver jwtResolver;
    private final JwtProvider jwtProvider;

    @Override
    public TokenCollection newTokenInfo(String refreshToken) {
        jwtResolver.validateRefreshToken(refreshToken);
        Long memberId = jwtResolver.getMemberIdFromRefreshToken(refreshToken);
        return jwtProvider.createTokenCollection(TokenInfo.from(memberId));
    }

    @Override
    public TokenCollection testLogin() {
        return jwtProvider.createTokenCollection(TokenInfo.from(1L));
    }
    @Override
    public TokenCollection testLogin2() {
        return jwtProvider.createTokenCollection(TokenInfo.from(2L));
    }
}
