package com.tools.seoultech.timoproject.auth.jwt.filter;

import com.tools.seoultech.timoproject.auth.jwt.HeaderTokenExtractor;
import com.tools.seoultech.timoproject.auth.jwt.JwtResolver;
import com.tools.seoultech.timoproject.auth.service.CustomUserDetailsService;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final HeaderTokenExtractor headerTokenExtractor;
    private final JwtResolver jwtResolver;

    private static final List<RequestMatcher> whiteListPatternsForApi = List.of(
            new AntPathRequestMatcher("/api/v1/auth/**"),
            new AntPathRequestMatcher("/api/v1/chat/**"),
            new AntPathRequestMatcher("/api/v1/members/player/verify"),
            new AntPathRequestMatcher("/api/v1/members/nickname/**"),
            new RegexRequestMatcher("^/api/v1/members/[0-9]+$", "GET"),
            new AntPathRequestMatcher("/naver/callback"),
            new AntPathRequestMatcher("/kakao/callback"),
            new AntPathRequestMatcher("/discord/callback"),
            new AntPathRequestMatcher("/api/v1/posts/public/**"),
            new AntPathRequestMatcher("/api/v1/comments/public/**"),
            new AntPathRequestMatcher("/bower_components/**"),
            new AntPathRequestMatcher("/dist/**"),
            new AntPathRequestMatcher("/plugins/**"),
            new AntPathRequestMatcher("/css/**"),
            new AntPathRequestMatcher("/js/**"),
            new AntPathRequestMatcher("/img/**"),
            new AntPathRequestMatcher("/actuator/**")

    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = headerTokenExtractor.extractAccessToken(request);
        checkAccessTokenValidation(accessToken);
        setAuthenticationInSecurityContext(accessToken);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        List<RequestMatcher> skipList = new ArrayList<>();
        skipList.addAll(whiteListPatternsForApi);

        OrRequestMatcher orRequestMatcher = new OrRequestMatcher(new ArrayList<>(skipList));
        return orRequestMatcher.matches(request);
    }

    private void checkAccessTokenValidation(String accessToken) {
        if(!StringUtils.hasText(accessToken)) {
            log.error("Access token is empty");
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN_EXCEPTION);
        }
        if(!jwtResolver.validateAccessToken(accessToken)) {
            log.error("Access token is invalid");
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN_EXCEPTION);
        }
    }

    private void setAuthenticationInSecurityContext(String accessToken) {
        try{
            Long memberId = jwtResolver.getMemberIdFromAccessToken(accessToken);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(memberId.toString());
            Authentication authentication = getAuthenticationFromUserDetails(userDetails);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch(UsernameNotFoundException e) {
            log.error("User not found");
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN_EXCEPTION);
        }
    }

    private static UsernamePasswordAuthenticationToken getAuthenticationFromUserDetails(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );
    }
}
