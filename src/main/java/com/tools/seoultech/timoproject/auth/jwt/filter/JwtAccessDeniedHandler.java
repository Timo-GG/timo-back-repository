package com.tools.seoultech.timoproject.auth.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.seoultech.timoproject.global.APIErrorResponse;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("member id {} has Role {}",
            SecurityContextHolder.getContext().getAuthentication().getName(),
            SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        log.error("error : {}", accessDeniedException.getMessage(), accessDeniedException);
        ErrorCode errorCode = ErrorCode.FORBIDDEN_EXCEPTION;
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());
        response.getWriter()
                .write(objectMapper.writeValueAsString(
                        new APIErrorResponse(false, errorCode.getCode(), errorCode.getMessage())));
    }
}
