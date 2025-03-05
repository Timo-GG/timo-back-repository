package com.tools.seoultech.timoproject.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")        // 모든 경로
                .allowedOrigins("*")      // 모든 Origin 허용
                .allowedMethods("*")      // 모든 HTTP Method 허용
                .allowedHeaders("*")      // 모든 헤더 허용 (필요 시)
                .allowCredentials(true);
    }
}
