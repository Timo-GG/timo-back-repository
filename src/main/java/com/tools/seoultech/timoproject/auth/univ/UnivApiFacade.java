package com.tools.seoultech.timoproject.auth.univ;

import com.tools.seoultech.timoproject.global.APIErrorResponse;

public interface UnivApiFacade {
    void checkUniv(UnivRequestDTO requestDto) throws Exception;
    Boolean certify(UnivRequestDTO requestDto) throws Exception;
    Boolean verify(UnivRequestDTO requestDto, Integer code) throws Exception;
    Object getVerifiedUserList() throws Exception;
    Object checkStatus(UnivRequestDTO requestDto) throws Exception;
}
