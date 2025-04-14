package com.tools.seoultech.timoproject.auth.univ;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UnivApiFacadeImpl implements UnivApiFacade {
    private final UnivService univService;

    @Override
    public void checkUniv(UnivRequestDTO requestDto) throws Exception {
        univService.checkUniv(requestDto.univName());
    }

    @Override
    public void certify(UnivRequestDTO requestDto) throws IOException {
        univService.checkUniv(requestDto.univName());
        univService.certifyUniv(requestDto);
    }

    @Override
    public void verify(UnivRequestDTO requestDto, Integer code) throws IOException {
        univService.verifyRequest(requestDto, code);
    }
    @Override
    public Object getVerifiedUserList() throws Exception {
        return univService.getVerifiedUserList();
    }

    @Override
    public Object checkStatus(UnivRequestDTO requestDto) throws Exception {
        return univService.checkStatus(requestDto);
    }
}
