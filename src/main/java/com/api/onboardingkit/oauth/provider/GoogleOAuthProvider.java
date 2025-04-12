package com.api.onboardingkit.oauth.provider;

import com.api.onboardingkit.global.response.exception.CustomException;
import com.api.onboardingkit.global.response.exception.ErrorCode;
import com.api.onboardingkit.oauth.dto.SocialUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class GoogleOAuthProvider implements OAuthProvider {
    private static final String GOOGLE_TOKEN_INFO_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=";

    @Override
    public SocialUserInfo validateToken(String token) {
        String url = GOOGLE_TOKEN_INFO_URL + token;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            String id = response.getBody().get("sub").toString();
            String email = response.getBody().get("email").toString();
            return new SocialUserInfo(id, email);
        }
        throw new CustomException(ErrorCode.GOOGLE_ID_VALIDATE_FAILED);
    }

}
