package com.tools.seoultech.timoproject.auth.univ;


import java.io.IOException;

public interface UnivApiFacade {
    void checkUniv(UnivRequestDTO requestDto);
    Boolean certify(UnivRequestDTO requestDto) throws IOException;
    Boolean verify(UnivRequestDTO requestDto, Integer code, Long memberId); // ⭐️ 수정
    void deleteCertifiedUniv(Long memberId);
}
