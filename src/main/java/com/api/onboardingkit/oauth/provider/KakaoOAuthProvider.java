package com.api.onboardingkit.oauth.provider;

import com.api.onboardingkit.global.response.exception.CustomException;
import com.api.onboardingkit.global.response.exception.ErrorCode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class KakaoOAuthProvider implements OAuthProvider {
    private static final String KAKAO_OAUTH_URL = "https://kapi.kakao.com/v2/user/me";

    @Override
    public String validateToken(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> res = restTemplate.postForEntity(KAKAO_OAUTH_URL, entity, Map.class);

        if (res.getStatusCode().is2xxSuccessful() && res.getBody() != null) {
            return res.getBody().get("id").toString(); // Kakao 사용자 ID 반환
        }
        throw new CustomException(ErrorCode.KAKAO_ID_VALIDATE_FAILED);
    }
}
