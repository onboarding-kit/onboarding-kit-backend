package com.api.onboardingkit.oauth.provider;

import com.api.onboardingkit.global.response.exception.CustomException;
import com.api.onboardingkit.global.response.exception.ErrorCode;
import com.api.onboardingkit.oauth.dto.SocialUserInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Slf4j
@Component
public class AppleOAuthProvider implements OAuthProvider {
    private static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys";

    @Override
    public SocialUserInfo validateToken(String token) {
        try{
            // Apple의 공개 키 가져오기
            PublicKey publicKey = getApplePublicKey(token);

            // JWT 검증
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            // Apple의 sub 값(사용자 ID)과 이메일 반환
            String email = claims.get("email", String.class);
            String id = claims.getSubject();

            return new SocialUserInfo(id, email);
        }catch (Exception e){
            log.error("Apple 토큰 검증 실패: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.APPLE_ID_VALIDATE_FAILED);
        }
    }

    private PublicKey getApplePublicKey(String token) throws Exception {
        // 토큰 서명에 사용된 Apple의 공개 키를 가져오기
        // https://developer.apple.com/documentation/accountorganizationaldatasharing/fetch-apple's-public-key-for-verifying-token-signature

        // Apple의 공개 키 목록 가져오기
        // https://developer.apple.com/documentation/accountorganizationaldatasharing/jwkset/jwkset.keys
        RestTemplate restTemplate = new RestTemplate();
        String res = restTemplate.getForObject(APPLE_PUBLIC_KEYS_URL, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode keys = objectMapper.readTree(res).get("keys");

        // JWT Header에서 kid, alg 값 가져오기
        String[] jwtParts = token.split("\\.");
        String headerJson = new String(Base64.getUrlDecoder().decode(jwtParts[0]), StandardCharsets.UTF_8);
        JsonNode header = objectMapper.readTree(headerJson);

        String kid = header.get("kid").asText();
        String alg = header.get("alg").asText();

        // kid, alg과 일치하는 공개 키 찾기
        for(JsonNode key : keys){
            if(key.get("kid").asText().equals(kid) && key.get("alg").asText().equals(alg)){
                return generatePublicKey(
                        key.get("n").asText(), // RSA 공개 키의 모듈러스 값
                        key.get("e").asText() // RSA 공개 키의 지수 값
                );
            }
        }
        throw new CustomException(ErrorCode.NOT_FOUND_APPLE_PUBLIC_KEY);
    }

    private PublicKey generatePublicKey(String modulusBase64, String exponentBase64) throws CustomException, NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(modulusBase64));
        BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(exponentBase64));

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
