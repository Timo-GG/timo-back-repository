package com.tools.seoultech.timoproject.auth.univ;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UnivApiFacadeImpl implements UnivApiFacade {
    private final UnivService univService;


    @Override
    public void checkUniv(UnivRequestDTO requestDto) {
        univService.checkUniv(requestDto.univName());
    }

    @Override
    public Boolean certify(UnivRequestDTO requestDto) throws IOException {
        return univService.certifyUniv(requestDto);
    }

    @Override
    public Boolean verify(UnivRequestDTO requestDto, Integer code, Long memberId) {
        return univService.verifyRequest(requestDto, code, memberId);
    }

    @Override
    public void deleteCertifiedUniv(Long memberId) {
        univService.deleteCertifiedMember(memberId);
    }
}
