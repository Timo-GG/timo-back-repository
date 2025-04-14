package com.tools.seoultech.timoproject.global.config;

import com.tools.seoultech.timoproject.global.interceptor.LoggingInterceptor;
import com.tools.seoultech.timoproject.global.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/admin/v1/**") // /admin 경로에만 적용
                .excludePathPatterns("/admin/v1/login", "/admin/v1/**", "/resources/**", "/error"); // 예외 처리

        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**"); // 모든 경로에 적용
    }
}
