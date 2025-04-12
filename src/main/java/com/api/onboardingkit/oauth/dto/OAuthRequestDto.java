package com.api.onboardingkit.oauth.dto;

import lombok.Getter;

@Getter
public class OAuthRequestDto {
    private String socialType; // google, kakao, apple
    private String token; // 앱에서 받은 소셜 로그인 액세스 토큰
}
