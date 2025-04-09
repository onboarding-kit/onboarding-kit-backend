package com.api.onboardingkit.oauth.provider;

import com.api.onboardingkit.oauth.dto.SocialUserInfo;

public interface OAuthProvider {
    SocialUserInfo validateToken(String token);
}
