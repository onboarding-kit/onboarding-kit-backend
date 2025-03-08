package com.api.onboardingkit.oauth.dto;

import lombok.Getter;

@Getter
public class OAuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public OAuthResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}