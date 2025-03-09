package com.api.onboardingkit.oauth.provider;

public interface OAuthProvider {
    String validateToken(String token);
}