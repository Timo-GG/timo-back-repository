package com.tools.seoultech.timoproject.auth.univ;

import com.univcert.api.UnivCert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class UnivService {
     @Value("${univ_api_key}")
     private String api_key;

     public void checkUniv(String univName) throws IOException{
          Map<String, Object> response = UnivCert.check(univName);
          if(response.get("success") == "false"){
               throw new IOException(response.get("message").toString());
          }
          System.err.println(response.toString());
     }

     public void certifyUniv(UnivRequestDTO requestDto) throws IOException {
          Map<String, Object> response = UnivCert.certify(api_key, requestDto.univEmail(), requestDto.univName(), true);
          if(response.get("success") == "false"){
               throw new IOException(response.get("message").toString());
          }
          System.err.println(response.toString());
     }
     public void verifyRequest(UnivRequestDTO requestDto, int code) throws IOException {
          Map<String, Object> response = UnivCert.certifyCode(api_key, requestDto.univEmail(), requestDto.univName(), code);
          System.err.println(response.toString());
     }
}
