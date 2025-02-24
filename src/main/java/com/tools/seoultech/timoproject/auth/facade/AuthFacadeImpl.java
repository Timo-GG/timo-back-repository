package com.tools.seoultech.timoproject.auth.facade;

import com.tools.seoultech.timoproject.auth.jwt.JwtProvider;
import com.tools.seoultech.timoproject.auth.jwt.JwtResolver;
import com.tools.seoultech.timoproject.auth.jwt.TokenCollection;
import com.tools.seoultech.timoproject.auth.jwt.TokenInfo;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.riot.service.BasicAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacadeImpl implements AuthFacade {

    private final JwtResolver jwtResolver;
    private final JwtProvider jwtProvider;
    private final BasicAPIService riotService;

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
    public AccountDto.Response verifyPlayer(AccountDto.Request request){
        try{
            AccountDto.Response response = riotService.findUserAccount(request);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
