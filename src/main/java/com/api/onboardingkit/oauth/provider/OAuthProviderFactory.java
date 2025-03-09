package com.api.onboardingkit.oauth.provider;

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
        if (!providers.containsKey(provider)) {
            throw new RuntimeException("지원하지 않는 OAuth Provider: " + provider);
        }
        return providers.get(provider);
    }
}