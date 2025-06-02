package com.tools.seoultech.timoproject.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // 로그 메시지 빌더 초기화
        StringBuilder logMessage = new StringBuilder(String.format("Request Method: [%s] URL: [%s]", method, uri));

        // 1. Body 로그 처리 (CustomHttpRequestWrapper를 사용한 경우)
        if (request instanceof CustomHttpRequestWrapper requestWrapper) {
            String requestBody = new String(requestWrapper.getRequestBody());
            if (!requestBody.isEmpty()) {
                logMessage.append(String.format(" Body: [%s]", requestBody));
            }
        }

        // 2. Path Variable 로그 처리
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables != null && !pathVariables.isEmpty()) {
            logMessage.append(String.format(" Path Variables: [%s]", pathVariables));
        }

        // 3. Request Parameter 로그 처리
        Map<String, String> params = getRequestParams(request);
        if (!params.isEmpty()) {
            logMessage.append(String.format(" Params: [%s]", params));
        }

        // 최종 로그 출력
        log.info(logMessage.toString());
        return true;
    }

    // 요청 파라미터를 Map으로 변환하는 헬퍼 메서드
    private Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            paramMap.put(paramName, request.getParameter(paramName));
        }
        return paramMap;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        // 응답 로깅
        log.info("Response Status: [{}]", response.getStatus());

        if(handler instanceof HandlerMethod handlerMethod){
            log.info("Directed Controller Class: [{}]",handlerMethod.getClass().getName());
            log.info("Directed Controller Method: [{}]",handlerMethod.getMethod().getName());
        }

    }
}
