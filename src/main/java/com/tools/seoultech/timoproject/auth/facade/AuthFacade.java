package com.tools.seoultech.timoproject.auth.facade;

import com.tools.seoultech.timoproject.auth.jwt.TokenCollection;

public interface AuthFacade {

    TokenCollection newTokenInfo(String refreshToken);
}
