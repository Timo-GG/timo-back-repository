package com.tools.seoultech.timoproject.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            // 메서드에 @LoginRequired가 붙어 있는지 확인
            LoginRequired methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequired.class);
            // 클래스에 @LoginRequired가 붙어 있는지 확인
            LoginRequired classAnnotation = handlerMethod.getBeanType().getAnnotation(LoginRequired.class);

            if (methodAnnotation != null || classAnnotation != null) {
                // 세션에서 로그인 여부 확인
                Object isAdmin = request.getSession().getAttribute("isAdmin");
                if (isAdmin == null || !(Boolean) isAdmin) {
                    response.sendRedirect("/admin/login"); // 로그인 페이지로 리다이렉트
                    return false; // 요청 중단
                }
            }
        }
        return true; // 통과
    }
}
