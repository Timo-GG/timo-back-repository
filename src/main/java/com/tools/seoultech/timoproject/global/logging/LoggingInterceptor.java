package com.tools.seoultech.timoproject.global.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.seoultech.timoproject.global.logging.response.LoggingResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 요청 로깅
        log.info("Request Method: [{}] URL: [{}]",request.getMethod(), request.getRequestURI());
        return true;  // 요청을 계속 진행
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        // 응답 로깅
        log.info("Response Status: [{}]", response.getStatus());

        if(handler instanceof HandlerMethod handlerMethod){
            log.info("Directed Controller Class: [{}]",handlerMethod.getClass().getName());
            log.info("Directed Controller Method: [{}]",handlerMethod.getMethod().getName());
        }
        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws JsonProcessingException {
            // Response Logging
            if (response instanceof LoggingResponseWrapper responseWrapper) {
                byte[] responseData = responseWrapper.getResponseData();
                if (responseData != null && responseData.length > 0) {
                    String responseBody = new String(responseData);

                    ObjectMapper mapper = new ObjectMapper();
                    Object json = mapper.readValue(responseBody, Object.class);
                    String prettyBody = mapper.writeValueAsString(json);

                    log.info("Response Status: [{}] URL: [{}] Body: [{}]", response.getStatus(), request.getRequestURI(), prettyBody);
                } else {
                    log.info("Response Status: [{}] URL: [{}] Body: [Empty]", response.getStatus(), request.getRequestURI());
                }
            } else {
                log.info("Response Status: [{}] URL: [{}]", response.getStatus(), request.getRequestURI());
            }
        }

    }
}
