package com.tools.seoultech.timoproject.auth.jwt;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtResolver {
    private final JwtProperty jwtProperty;
    private Key accessKey;
    private Key refreshKey;

    @PostConstruct
    public void init() {
        byte[] accessKeyBytes = jwtProperty.getAccessKey().getBytes(StandardCharsets.UTF_8);
        byte[] refreshKeyBytes = jwtProperty.getRefreshKey().getBytes(StandardCharsets.UTF_8);
        accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
        refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    public Long getMemberIdFromAccessToken(String accessToken) {
        try {
            Claims claims = getAccessTokenBody(accessToken);
            return Long.parseLong(claims.get("memberId").toString());
        } catch (ExpiredJwtException e) {
            throw new BusinessException(e, ErrorCode.EXPIRED_ACCESS_TOKEN_EXCEPTION);
        } catch (Exception e) {
            throw new BusinessException(e, ErrorCode.INVALID_ACCESS_TOKEN_EXCEPTION);
        }
    }
    public Long getMemberIdFromRefreshToken(String refreshToken) {
        try {
            Claims claims = getRefreshTokenBody(refreshToken);
            return Long.parseLong(claims.get("memberId").toString());
        } catch (ExpiredJwtException e) {
            throw new BusinessException(e, ErrorCode.EXPIRED_REFRESH_TOKEN_EXCEPTION);
        } catch (Exception e) {
            throw new BusinessException(e, ErrorCode.INVALID_REFRESH_TOKEN_EXCEPTION);
        }
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            return !getAccessTokenBody(accessToken)
                    .getExpiration()
                    .before(new Date());
        } catch (SecurityException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            throw new BusinessException(e, ErrorCode.INVALID_ACCESS_TOKEN_EXCEPTION);
        }catch (UnsupportedJwtException e) {
            throw new BusinessException(e, ErrorCode.UNSUPPORTED_JWT_TOKEN_EXCEPTION);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(e, ErrorCode.EXPIRED_ACCESS_TOKEN_EXCEPTION);
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            return !getRefreshTokenBody(refreshToken)
                    .getExpiration()
                    .before(new Date());
        } catch (SecurityException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            throw new BusinessException(e, ErrorCode.INVALID_REFRESH_TOKEN_EXCEPTION);
        } catch (UnsupportedJwtException e) {
            throw new BusinessException(e, ErrorCode.UNSUPPORTED_JWT_TOKEN_EXCEPTION);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(e, ErrorCode.EXPIRED_REFRESH_TOKEN_EXCEPTION);
        }
    }

    private Claims getAccessTokenBody(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }

    private Claims getRefreshTokenBody(String refreshToken) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
    }

}
