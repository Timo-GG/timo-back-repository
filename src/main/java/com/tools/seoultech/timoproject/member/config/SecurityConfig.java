package com.tools.seoultech.timoproject.member.config;

import com.tools.seoultech.timoproject.auth.jwt.JwtAuthenticationFilter;
import com.tools.seoultech.timoproject.auth.jwt.JwtTokenProvider;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    // ── 2. 일반 사용자용 Security Filter Chain (JWT 인증 추가) ─────────────────────────────
    @Bean
    @Order(2)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http,
                                                       com.tools.seoultech.timoproject.member.service.OAuth2MemberService oAuth2MemberService,
                                                       JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) throws Exception {
        http
                // JWT 필터를 UsernamePasswordAuthenticationFilter 전에 추가하여 모든 요청에서 JWT를 검증합니다.
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, memberRepository), UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/v1/**", "/api/auth/**"))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/oauth2/**", "/login/**", "/api/auth/**").permitAll()
                        .requestMatchers("/api/v1/**", "api/members/**").authenticated()
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
