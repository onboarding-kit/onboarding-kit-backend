package com.api.onboardingkit.oauth.dto;

import lombok.Getter;

@Getter
public class TokenReissueRequestDto {
    private String refreshToken;
}
