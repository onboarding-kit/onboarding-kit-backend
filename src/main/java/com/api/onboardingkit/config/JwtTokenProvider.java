package com.api.onboardingkit.config;

import com.api.onboardingkit.config.exception.CustomException;
import com.api.onboardingkit.config.exception.ErrorCode;
import com.api.onboardingkit.member.entity.SocialType;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import io.jsonwebtoken.security.Keys;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenProvider {

    private String secret;
    private Key key;

    private long accessTokenExpiration;
    private long refreshTokenExpiration;

    @PostConstruct
    public void init() {
        Dotenv dotenv = Dotenv.load();
        this.secret = dotenv.get("JWT_SECRET_KEY");
        if(this.secret == null || secret.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_JWT_SECRET);
        }

        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    public String generateToken(Long memberId, SocialType socialType) {
        return createToken(memberId, socialType, accessTokenExpiration);
    }

    public String generateRefreshToken(Long memberId, SocialType socialType) {
        return createToken(memberId, socialType, refreshTokenExpiration);
    }

    private String createToken(Long memberId, SocialType socialType, long expiration) {
        return Jwts.builder()
                .setSubject(memberId.toString())
                .claim("socialType", socialType.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.JWT_EXPIRED);
        } catch (MalformedJwtException e) {
            throw new CustomException(ErrorCode.JWT_PARSING_FAILED);
        } catch (SignatureException e) {
            throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(ErrorCode.JWT_PARSING_FAILED);
        }
    }

    public String getMemberIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject(); // sub, memberId 값
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
