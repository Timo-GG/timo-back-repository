package com.tools.seoultech.timoproject.global.filter;

import com.tools.seoultech.timoproject.global.interceptor.CustomHttpRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.LogRecord;

@Component
public class RequestWrapperFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            CustomHttpRequestWrapper requestWrapper = new CustomHttpRequestWrapper(httpRequest);
            chain.doFilter(requestWrapper, response);  // CustomHttpRequestWrapper로 감싼 후 체인 진행
        } else {
            chain.doFilter(request, response);
        }
    }
}
