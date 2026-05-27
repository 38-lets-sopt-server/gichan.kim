package org.sopt.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.sopt.domain.auth.code.AuthErrorCode;
import org.sopt.global.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey key;
    private final long accessTokenExpiresInSeconds;
    private final long refreshTokenExpiresInSeconds;

    public JwtProvider(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.access-token-expires-in-seconds:1800}") long accessTokenExpiresInSeconds,
            @Value("${security.jwt.refresh-token-expires-in-seconds:1209600}") long refreshTokenExpiresInSeconds
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiresInSeconds = accessTokenExpiresInSeconds;
        this.refreshTokenExpiresInSeconds = refreshTokenExpiresInSeconds;
    }

    public String generateAccessToken(Long memberId, String email) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .claim("email", email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTokenExpiresInSeconds)))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(Long memberId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(refreshTokenExpiresInSeconds)))
                .signWith(key)
                .compact();
    }

    public Long verifyAndGetMemberId(String token) {
        if (token == null || token.isBlank()) {
            throw new CustomException(AuthErrorCode.EMPTY_ACCESS_TOKEN);
        }
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Long.parseLong(claims.getSubject());
        } catch (ExpiredJwtException e) {
            throw new CustomException(AuthErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (JwtException e) {
            throw new CustomException(AuthErrorCode.INVALID_ACCESS_TOKEN);
        } catch (NumberFormatException e) {
            throw new CustomException(AuthErrorCode.INVALID_ACCESS_TOKEN_SUBJECT);
        }
    }

    public long getRemainingExpiration(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Date expiration = claims.getExpiration();
            long now = System.currentTimeMillis();

            return expiration.getTime() - now;

        } catch (ExpiredJwtException e) {
            return 0;
        } catch (JwtException e) {
            throw new CustomException(AuthErrorCode.INVALID_ACCESS_TOKEN);
        }
    }
}
