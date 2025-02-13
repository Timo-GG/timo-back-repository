package com.tools.seoultech.timoproject.member.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    // ── 1. 관리자용 Security Filter Chain ─────────────────────────────
    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        // /admin/v1/** URL에 대해서는 Spring Security의 인증 처리 없이 모두 허용
        http
                .securityMatcher("/admin/v1/**")
                // 필요한 경우, 관리자 관련 CSRF 설정 (여기선 예외로 처리)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/admin/v1/posts/delete/**"))
                // 관리자 엔드포인트는 모두 허용(인증/인가 처리는 직접 구현한 로직에서 처리)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                // Spring Security의 formLogin, httpBasic 등 인증 관련 기능 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // ── 2. 일반 사용자용 Security Filter Chain ─────────────────────────────
    @Bean
    @Order(2)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http,
                                                       com.tools.seoultech.timoproject.member.service.OAuth2MemberService oAuth2MemberService) throws Exception {
        http
                // API 관련 CSRF 예외
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/v1/**"))
                // 폼 로그인 및 HTTP 기본 인증 비활성화 (OAuth2 전용)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // OAuth2 로그인 설정 (일반 사용자용)
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2MemberService)
                        )
                )
                // URL별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/oauth2/**", "/login/**").permitAll()  // 로그인 관련 및 공개 URL 허용
                        .requestMatchers("/api/v1/**").authenticated()                 // API 요청은 인증 필요
                        // 정적 리소스 접근 허용
                        .requestMatchers(
                                "/bower_components/**",
                                "/dist/**",
                                "/plugins/**",
                                "/css/**",
                                "/js/**",
                                "/img/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
