package com.api.onboardingkit.config;

import com.api.onboardingkit.global.response.exception.CustomException;
import com.api.onboardingkit.global.response.exception.ErrorCode;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import io.jsonwebtoken.security.Keys;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Getter
public class JwtTokenProvider {
    private String secret;
    private Key key;
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30; //30분
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7일

    @PostConstruct
    public void init() {
        Dotenv dotenv = Dotenv.load();
        this.secret = dotenv.get("JWT_SECRET_KEY");
        if(this.secret == null || secret.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_JWT_SECRET);
        }

        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    public String generateToken(String userId) {
        return createToken(userId, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(String userId) {
        return createToken(userId, REFRESH_TOKEN_EXPIRATION);
    }

    private String createToken(String userId, long expiration) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(ErrorCode.TOKEN_VALIDATE_FAILED);
        }
    }

    public String getUserIdFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

}
