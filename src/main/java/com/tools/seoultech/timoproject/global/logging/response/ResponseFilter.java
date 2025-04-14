package com.tools.seoultech.timoproject.global.logging.response;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResponseFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        if(servletResponse instanceof HttpServletResponse response) {
            LoggingResponseWrapper responseWrapper = new LoggingResponseWrapper((HttpServletResponse) servletResponse);
            filterChain.doFilter(servletRequest, servletResponse);

            byte[] data = responseWrapper.getResponseData(); // outputDataStream은 아직 flush하지 않음.
            if(data != null || data.length > 0) { //
                response.getOutputStream().write(data);
                //TODO: 파일로 저장해야됨.
            } else{
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }
}
