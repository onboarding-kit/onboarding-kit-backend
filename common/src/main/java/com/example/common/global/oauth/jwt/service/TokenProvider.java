package com.example.common.global.oauth.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.common.global.exception.ExceptionMessage;
import com.example.common.global.oauth.jwt.Enum.TokenType;
import com.example.common.global.utils.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String BEARER = "Bearer ";

    private final RedisUtils redisUtils;

    /**
     * AccessToken 발행
     */
    public String issueAccessToken(Long userId){
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withClaim("id", userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * RefreshToken 발행
     */
    public String issueRefreshToken(Long userId){
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withClaim("id", userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * AccessToken 재발행
     */
    public String reissueAccessToken(String refreshToken){
        DecodedJWT decodedJWT;
        try{
            decodedJWT = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(refreshToken);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException(ExceptionMessage.TOKEN_VERIFICATION);
        }

        Long userId = decodedJWT.getClaim("id").asLong();
        Long value = redisUtils.getData(refreshToken);
        if (userId == null || value == null) {
            throw new AuthenticationServiceException(ExceptionMessage.TOKEN_NOT_FOUND);
        } else if (!userId.equals(value)) {
            throw new AuthenticationServiceException(ExceptionMessage.TOKEN_VERIFICATION);
        }
        return issueAccessToken(userId);
    }

    /**
     * RefreshToken 업데이트
     */
    public void updateRefreshToken(String refreshToken, Long userId) {
        redisUtils.setDateWithExpiration(refreshToken, userId, Duration.ofDays(refreshTokenExpirationPeriod));
    }

    /**
     * Token 추출
     */
    public String extractToken(HttpServletRequest request, TokenType tokenType){
        Optional<String> requestToken = Optional.empty();

        if (tokenType == TokenType.ACCESSTOKEN) {
            requestToken = Optional.ofNullable(request.getHeader(accessHeader))
                    .filter(token -> token.startsWith(BEARER))
                    .map(token -> token.substring(7));
        } else if (tokenType == TokenType.REFRESHTOKEN) {
            requestToken = Optional.ofNullable(request.getHeader(refreshHeader))
                    .filter(token -> token.startsWith(BEARER))
                    .map(token -> token.substring(7));
        }
        return requestToken.orElse(null);
    }

    /**
     * 토큰 복호화
     */
    public DecodedJWT decodeJWT(String accessToken, String refreshToken) {
        try {
            return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(accessToken);
        } catch (TokenExpiredException e) {
            log.info("AccessToken is expired: ${}", accessToken);
            String newAccessToken = reissueAccessToken(refreshToken);
            return decodeJWT(newAccessToken, refreshToken);
        } catch (JWTVerificationException e){
            throw new JWTVerificationException(ExceptionMessage.TOKEN_VERIFICATION);
        }
    }
}
