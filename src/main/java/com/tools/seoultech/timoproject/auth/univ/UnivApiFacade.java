package com.tools.seoultech.timoproject.auth.univ;

import com.tools.seoultech.timoproject.global.APIErrorResponse;

public interface UnivApiFacade {
    void certify(UnivRequestDTO requestDto) throws Exception;
    void verify(UnivRequestDTO requestDto, Integer code) throws Exception;
}
