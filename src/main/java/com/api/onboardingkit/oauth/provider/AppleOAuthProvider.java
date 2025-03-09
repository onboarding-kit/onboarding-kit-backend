package com.api.onboardingkit.oauth.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Component
public class AppleOAuthProvider implements OAuthProvider {

    private static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys";

    @Override
    public String validateToken(String token) {
        try {
            // Apple의 공개 키 가져오기
            PublicKey publicKey = getApplePublicKey(token);

            // JWT 검증
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // Apple의 `sub` 값 (사용자 ID) 반환
        } catch (Exception e) {
            throw new RuntimeException("Invalid Apple ID Token", e);
        }
    }

    private PublicKey getApplePublicKey(String token) throws Exception {
        // Apple의 공개 키 목록 가져오기
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(APPLE_PUBLIC_KEYS_URL, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode keys = objectMapper.readTree(response).get("keys");

        // JWT Header에서 kid, alg 값 가져오기
        String[] jwtParts = token.split("\\.");
        String headerJson = new String(Base64.getUrlDecoder().decode(jwtParts[0]), StandardCharsets.UTF_8);
        JsonNode header = objectMapper.readTree(headerJson);

        String kid = header.get("kid").asText();
        String alg = header.get("alg").asText();

        // kid와 일치하는 공개 키 찾기
        for (JsonNode key : keys) {
            if (key.get("kid").asText().equals(kid) && key.get("alg").asText().equals(alg)) {
                return generatePublicKey(
                        key.get("n").asText(),
                        key.get("e").asText()
                );
            }
        }
        throw new RuntimeException("Apple public key not found");
    }

    private PublicKey generatePublicKey(String modulusBase64, String exponentBase64) throws Exception {
        BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(modulusBase64));
        BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(exponentBase64));

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}