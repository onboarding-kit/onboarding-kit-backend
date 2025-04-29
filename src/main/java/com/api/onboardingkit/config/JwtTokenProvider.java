package com.api.onboardingkit.config;

import com.api.onboardingkit.config.exception.CustomException;
import com.api.onboardingkit.config.exception.ErrorCode;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
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

    public String generateToken(String socialId, String socialType) {
        return createToken(socialId, socialType, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(String socialId) {
        return createToken(socialId, null, REFRESH_TOKEN_EXPIRATION);
    }

    private String createToken(String socialId, String socialType, long expiration) {
        return Jwts.builder()
                .setSubject(socialId)
                .claim("socialType", socialType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(ErrorCode.TOKEN_VALIDATE_FAILED);
        }
    }

    public String getSocialIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject(); // sub, socialId 값
    }

    public String getSocialTypeFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get("socialType", String.class); // socialType(provider) 값
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
