package com.api.onboardingkit.oauth.provider;

import com.api.onboardingkit.config.exception.CustomException;
import com.api.onboardingkit.config.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OAuthProviderFactory {
    private final Map<String, OAuthProvider> providers;

    public OAuthProviderFactory(GoogleOAuthProvider google, KakaoOAuthProvider kakao, AppleOAuthProvider apple) {
        this.providers = Map.of(
                "google", google,
                "kakao", kakao,
                "apple", apple
        );
    }

    public OAuthProvider getProvider(String provider) {
        if(!providers.containsKey(provider)) {
            throw new CustomException(ErrorCode.NOT_ALLOWED_OAUTH_PROVIDER);
        }
        return providers.get(provider);
    }
}
