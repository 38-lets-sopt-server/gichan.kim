package org.sopt.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sopt.domain.auth.code.AuthErrorCode;
import org.sopt.global.exception.CustomException;
import org.sopt.global.jwt.service.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtProvider jwtProvider;
    private final HandlerExceptionResolver resolver;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtAuthFilter(
            JwtProvider jwtProvider,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver,
            TokenBlacklistService tokenBlacklistService
    ) {
        this.jwtProvider = jwtProvider;
        this.resolver = resolver;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(BEARER_PREFIX.length()).trim();

        try {
            if (tokenBlacklistService.isBlacklisted(token)) {
                throw new CustomException(AuthErrorCode.INVALID_ACCESS_TOKEN);
            }

            Long userId = jwtProvider.verifyAndGetMemberId(token);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userId, null, Collections.emptyList());

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);

        }
        catch (CustomException ex){
            resolver.resolveException(request, response, null, ex);
        }
    }
}
