package com.api.onboardingkit.oauth.provider;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class KakaoOAuthProvider implements OAuthProvider {

    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    @Override
    public String validateToken(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(KAKAO_USER_INFO_URL, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().get("id").toString(); // Kakao 사용자 ID 반환
        }
        throw new RuntimeException("Invalid Kakao Access Token");
    }
}