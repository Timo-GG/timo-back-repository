package com.tools.seoultech.timoproject.auth.univ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.univcert.api.UnivCert;
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

     public void checkUniv(String univName) throws IOException{
          Map<String, Object> response = UnivCert.check(univName);
          if(response.get("success").toString().equals("false")){
               throw new IOException(response.get("message").toString());
          }
          System.err.println(response.toString());
     }

     public void certifyUniv(UnivRequestDTO requestDto) throws IOException {
          Map<String, Object> response = UnivCert.certify(api_key, requestDto.univEmail(), requestDto.univName(), true);
          System.out.println("✅ UnivCert 응답 전체: " + response);
          if (response.get("success").toString().equals("false")) {
               String message = response.get("message").toString();
               if ("400".equals(response.get("code").toString())) {
                    throw new BusinessException(ErrorCode.UNIV_ALREADY_VERIFIED);
               }
               throw new IOException(message);
          }
     }
     public void verifyRequest(UnivRequestDTO requestDto, int code) throws IOException {
          Map<String, Object> response = UnivCert.certifyCode(api_key, requestDto.univEmail(), requestDto.univName(), code);
          System.err.println("[verifyRequest] response: " + response);
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

}
