package com.tools.seoultech.timoproject.auth.univ;

import com.fasterxml.jackson.databind.ObjectMapper;
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
     private final ObjectMapper objectMapper;

     public void checkUniv(String univName) throws IOException{
          Map<String, Object> response = UnivCert.check(univName);
          if(response.get("success").toString().equals("false")){
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
