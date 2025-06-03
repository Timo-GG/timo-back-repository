package com.tools.seoultech.timoproject.auth.univ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.member.MemberRepository;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.CertifiedUnivInfo;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.univcert.api.UnivCert;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UnivService {
     @Value("${univ_api_key}")
     private String api_key;

     private final MemberService memberService;
     private final MemberRepository memberRepository;
     private final EntityManager entityManager;

     public void checkUniv(String univName) throws IOException{
          Map<String, Object> response = UnivCert.check(univName);
          if(response.get("success").toString().equals("false")){
               throw new IOException(response.get("message").toString());
          }
          System.err.println("checkUniv: " + response.toString());
     }

     public Boolean certifyUniv(UnivRequestDTO requestDto) throws IOException {
          Map<String, Object> response = UnivCert.certify(api_key, requestDto.univEmail(), requestDto.univName(), true);
          if (response.get("success").toString().equals("false")) {
               String message = response.get("message").toString();
               return false;
          }
          return true;
     }
     public Boolean verifyRequest(UnivRequestDTO requestDto, int code) throws IOException {
          Map<String, Object> response = UnivCert.certifyCode(api_key, requestDto.univEmail(), requestDto.univName(), code);
          System.err.println("[verifyRequest] response: " + response);
          if (response.get("success").toString().equals("false")) {
               return false;
          }
          return true;
     }
     public Object checkStatus(UnivRequestDTO requestDto) throws IOException {
          Map<String, Object> response = UnivCert.status(api_key, requestDto.univEmail());
          if(response.get("success").toString().equals("false")){
               throw new IOException(response.get("message").toString());
          }
          return response;
     }
     public Object getVerifiedUserList() throws IOException {
          Map<String, Object> response = UnivCert.list(api_key);
          if(response.get("success").toString().equals("false")){
               throw new IOException(response.get("message").toString());
          }
          return response;
     }

     public void deleteCertifiedMember(Long memberId) throws IOException {
          Member member = memberService.getById(memberId);
          UnivCert.clear(api_key, member.getCertifiedUnivInfo().getUnivCertifiedEmail());
          member = member.toBuilder()
                  .certifiedUnivInfo(new CertifiedUnivInfo())
                  .build();
          memberRepository.save(member);

     }
}
