package com.api.onboardingkit.oauth.dto;

import lombok.Getter;

@Getter
public class SocialUserInfo {
    private String socialId;
    private String email;

    public SocialUserInfo(String socialId, String email) {
        this.socialId = socialId;
        this.email = email;
    }
}
