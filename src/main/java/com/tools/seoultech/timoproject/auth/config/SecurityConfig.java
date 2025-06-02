package com.tools.seoultech.timoproject.auth.config;

import com.tools.seoultech.timoproject.auth.jwt.HeaderTokenExtractor;
import com.tools.seoultech.timoproject.auth.jwt.JwtResolver;
import com.tools.seoultech.timoproject.auth.jwt.filter.JwtAccessDeniedHandler;
import com.tools.seoultech.timoproject.auth.jwt.filter.JwtAuthenticationFilter;
import com.tools.seoultech.timoproject.auth.jwt.filter.JwtExceptionFilter;
import com.tools.seoultech.timoproject.auth.service.CustomUserDetailsService;
import com.tools.seoultech.timoproject.global.config.WhitelistProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
                .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
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
                        .requestMatchers(new RegexRequestMatcher("^/api/v1/members/\\d+$", "GET")).permitAll()
                        .requestMatchers("/v3/api-docs", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
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

    /**
     *  CORS 설정 정의 : 웹 애플리케이션의 리소스가 다른 도메인에서 안전하게 접근 허용
     *  1. 모든 출처, 헤더, 메서드를 허용
     *  2. 자격 증명 허용 (예: 쿠키, 인증 헤더 등)
     *
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
