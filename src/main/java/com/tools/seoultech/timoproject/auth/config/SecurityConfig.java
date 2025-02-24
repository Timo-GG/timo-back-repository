package com.tools.seoultech.timoproject.auth.config;

import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.tools.seoultech.timoproject.auth.jwt.HeaderTokenExtractor;
import com.tools.seoultech.timoproject.auth.jwt.JwtResolver;
import com.tools.seoultech.timoproject.auth.jwt.filter.JwtAccessDeniedHandler;
import com.tools.seoultech.timoproject.auth.jwt.filter.JwtAuthenticationFilter;
import com.tools.seoultech.timoproject.auth.jwt.filter.JwtExceptionFilter;
import com.tools.seoultech.timoproject.auth.service.CustomUserDetailsService;
import com.tools.seoultech.timoproject.global.config.WhitelistProperties;
import com.tools.seoultech.timoproject.global.constant.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 새로 추가: 예외/권한 필터 관련

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    // ① 주입
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtResolver jwtResolver;
    private final HeaderTokenExtractor headerTokenExtractor;
    private final WhitelistProperties whitelistProperties;

    // 만약 corsConfigurationSource 등 다른 빈도 필요하면 주입

    // ── 1. 관리자용 Security Filter Chain ─────────────────────────────
    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        // /admin/v1/** URL에 대해서는 Spring Security의 인증 처리 없이 모두 허용
        http
                .securityMatcher("/admin/v1/**")
                .csrf(csrf -> csrf.ignoringRequestMatchers("/admin/v1/posts/delete/**"))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // ── 2. 일반 사용자용 Security Filter Chain (JWT 인증 추가) ─────────────────────────────
    @Bean
    @Order(2)
    public SecurityFilterChain userSecurityFilterChain(
            HttpSecurity http) throws Exception {

        // ② JwtAuthenticationFilter 생성
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(customUserDetailsService, headerTokenExtractor, jwtResolver);

        http
                // ③ 필터 등록: JWT 필터 → Exception 필터
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)

                // ④ 권한 부족 시 AccessDeniedHandler
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )

                // ⑤ 기타 설정
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/v1/**", "/api/v1/auth/**"))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // ⑥ URL 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // whitelist URL 적용
                        .requestMatchers(whitelistProperties.getUrls().toArray(new String[0])).permitAll()
                        .requestMatchers(HttpMethod.GET, whitelistProperties.getPublicUrls().toArray(new String[0])).permitAll()
                        // API 인증 필요 URL 설정
                        .requestMatchers("/api/v1/posts/**", "/api/v1/comments/**").authenticated()
                        .requestMatchers("/api/v1/**", "/api/v1/members/**").authenticated()
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
