package com.example.common.global.oauth.jwt.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.common.global.exception.BaseException;
import com.example.common.global.exception.ErrorCode;
import com.example.common.global.exception.ExceptionMessage;
import com.example.common.global.oauth.jwt.Enum.TokenType;
import com.example.common.global.oauth.jwt.service.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Value("${jwt.secret}")
    private String secretKey;

    private static final String[] whitelist = {
            "/", "/oauth**",
            "/resources/**", "/favicon.ico",
            "/swagger-ui/**", "/api-docs/**"
    };

    /**
     * 인증/인가 검사를 하지 않을 URI 체크(doFilterInternal 로직을 타지 않음)
     * @param request current HTTP request
     * @return true or false
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return PatternMatchUtils.simpleMatch(whitelist, request.getRequestURI());
    }

    /**
     * 인증/인가 검사
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = tokenProvider.extractToken(request, TokenType.ACCESSTOKEN);
        String refreshToken = tokenProvider.extractToken(request, TokenType.REFRESHTOKEN);

        if (accessToken == null || refreshToken == null) {
            log.error(ExceptionMessage.TOKEN_NOT_FOUND);
            throw new BaseException(ErrorCode.TOKEN_NOT_FOUND);
        }

        DecodedJWT decodedJWT = tokenProvider.decodeJWT(accessToken, refreshToken);
        Long id = decodedJWT.getClaim("id").asLong();
        Authentication authentication = new UsernamePasswordAuthenticationToken(id, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        doFilter(request, response, filterChain);
    }
}
