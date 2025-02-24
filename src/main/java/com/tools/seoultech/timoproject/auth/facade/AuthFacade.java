package com.tools.seoultech.timoproject.auth.facade;

import com.tools.seoultech.timoproject.auth.jwt.TokenCollection;
import com.tools.seoultech.timoproject.member.dto.AccountDto;

public interface AuthFacade {

    TokenCollection newTokenInfo(String refreshToken);

    TokenCollection testLogin();

    AccountDto.Response verifyPlayer(AccountDto.Request request);
}
