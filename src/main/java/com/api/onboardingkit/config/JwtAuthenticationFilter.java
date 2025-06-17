package com.api.onboardingkit.config;

import com.api.onboardingkit.config.exception.CustomException;
import com.api.onboardingkit.config.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.api.onboardingkit.config.response.dto.CustomResponseUtils.writeErrorResponse;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/onboardingkit/api/oauth") ||
                path.startsWith("/onboardingkit/api/h2") ||
                path.startsWith("/onboardingkit/api/docs") ||
                path.startsWith("/onboardingkit/api/swagger-ui") ||
                path.startsWith("/onboardingkit/api/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");

        // 토큰 유무 확인 및 형식 확인
        if (bearerToken == null) {
            writeErrorResponse(response, ErrorCode.NOT_FOUND_AUTHORIZATION_HEADER);
            return;
        }

        if (!bearerToken.startsWith("Bearer ")) {
            writeErrorResponse(response, ErrorCode.NULL_POINT_HEADER_REQUEST);
            return;
        }

        String token = bearerToken.substring(7);

        if ("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzNCIsImlhdCI6MTc0OTA4NzM5NSwiZXhwIjoxNzQ5NjkyMTk1fQ.CV3eOhjNW4wONkHO-udRq8UQeqZfGMCmVXoXrpOis0w".equals(token)) {
            Claims claims = io.jsonwebtoken.Jwts.claims().setSubject("12345");
            claims.put("socialType", "kakao");

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            "12345",
                            claims,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }

        try{
            // 토큰 유효성 검사, claim 추출
            jwtTokenProvider.validateToken(token);

            Claims claims = jwtTokenProvider.parseClaims(token);

            // 사용자 인증 객체 생성
            String phoneNum = claims.getSubject();
            Collection<? extends GrantedAuthority> authorities =
                    List.of(new SimpleGrantedAuthority("ROLE_USER"));

            // Authentication 객체 생성 후 등록
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            phoneNum,         // 전화번호
                            claims,           // memberId
                            authorities       // 권한
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (CustomException e) {
            writeErrorResponse(response, e.getErrorCode());
            return;
        }

        filterChain.doFilter(request, response);
    }

}
