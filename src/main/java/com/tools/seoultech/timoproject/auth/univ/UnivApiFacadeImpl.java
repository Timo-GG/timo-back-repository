package com.tools.seoultech.timoproject.auth.univ;

import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.CertifiedUnivInfo;
import com.tools.seoultech.timoproject.member.service.MemberService;
import jakarta.persistence.EntityManager;
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
    public Boolean certify(UnivRequestDTO requestDto) throws IOException {
        univService.checkUniv(requestDto.univName());
        Boolean result = univService.certifyUniv(requestDto);
        return result;
    }

    @Override
    public Boolean verify(UnivRequestDTO requestDto, Integer code) throws IOException {
        Boolean result = univService.verifyRequest(requestDto, code);
        return result;
    }
    @Override
    public Object getVerifiedUserList() throws Exception {
        return univService.getVerifiedUserList();
    }

    @Override
    public Object checkStatus(UnivRequestDTO requestDto) throws Exception {
        return univService.checkStatus(requestDto);
    }

    @Override
    public void deleteCertifiedUniv(Long memberId) throws Exception{
        univService.deleteCertifiedMember(memberId);
    }
}
