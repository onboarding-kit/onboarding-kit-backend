package com.api.onboardingkit.oauth.provider;

import com.api.onboardingkit.global.response.exception.CustomException;
import com.api.onboardingkit.global.response.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class GoogleOAuthProvider implements OAuthProvider {
    private static final String GOOGLE_TOKEN_INFO_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=";

    @Override
    public String validateToken(String token) {
        String url = GOOGLE_TOKEN_INFO_URL + token;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().get("sub").toString(); // Google 사용자 ID 반환
        }
        throw new CustomException(ErrorCode.GOOGLE_ID_VALIDATE_FAILED);
    }

}
