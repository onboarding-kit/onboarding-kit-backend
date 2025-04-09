package com.api.onboardingkit.oauth.provider;

import com.api.onboardingkit.global.response.exception.CustomException;
import com.api.onboardingkit.global.response.exception.ErrorCode;
import com.api.onboardingkit.oauth.dto.SocialUserInfo;
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
    public SocialUserInfo validateToken(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(KAKAO_OAUTH_URL, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            String email = response.getBody().get("email").toString();
            String id = response.getBody().get("id").toString();

            return new SocialUserInfo(id, email);
        }
        throw new CustomException(ErrorCode.KAKAO_ID_VALIDATE_FAILED);
    }
}
