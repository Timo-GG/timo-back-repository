package com.tools.seoultech.timoproject.auth.jwt;

import com.tools.seoultech.timoproject.auth.domain.CustomUserDetails;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = header.substring(7);
            JwtTokenProvider.TokenValidationResult validationResult = jwtTokenProvider.validate(token);
            if (validationResult != JwtTokenProvider.TokenValidationResult.VALID) {
                log.warn("JWT token validation failed: {}", validationResult);
                if (validationResult == JwtTokenProvider.TokenValidationResult.EXPIRED) {
                    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, 401, "Token expired");
                } else if (validationResult == JwtTokenProvider.TokenValidationResult.INVALID) {
                    sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, 403, "Invalid token");
                }
                return;
            }

            String subject = jwtTokenProvider.extractSubject(token);
            Long memberId = Long.valueOf(subject);
            Optional<Member> optionalMember = memberRepository.findById(memberId);
            if (optionalMember.isEmpty()) {
                log.warn("No member found for id: {}", memberId);
                sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, 403, "Member not found");
                return;
            }

            setAuthentication(optionalMember.get());
        } catch (Exception e) {
            log.error("Could not set user authentication in security context", e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 500, "Internal server error");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(Member member) {
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 에러 응답을 JSON 형태로 전송하는 공통 메서드
     */
    private void sendErrorResponse(HttpServletResponse response, int httpStatus, int errorCode, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(httpStatus);
        String jsonResponse = String.format("{\"success\": false, \"errorCode\": %d, \"message\": \"%s\"}", errorCode, message);
        response.getWriter().write(jsonResponse);
    }
}
