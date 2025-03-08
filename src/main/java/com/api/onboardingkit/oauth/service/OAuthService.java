package com.api.onboardingkit.oauth.service;

import com.api.onboardingkit.oauth.dto.OAuthRequestDto;
import com.api.onboardingkit.oauth.dto.OAuthResponseDto;
import com.api.onboardingkit.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public OAuthResponseDto authenticate(OAuthRequestDto request) {
        // Step 1: 받은 Access Token으로 소셜 API 호출하여 사용자 정보 확인 (추후 구현 필요)
        String socialUserId = validateSocialAccessToken(request.getProvider(), request.getToken());

        // Step 2: JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateToken(socialUserId);
        String refreshToken = jwtTokenProvider.generateRefreshToken(socialUserId);

        return new OAuthResponseDto(accessToken, refreshToken);
    }

    private String validateSocialAccessToken(String provider, String token) {
        // 실제로 Google, Kakao, Apple API를 호출하여 사용자 정보 검증해야 함
        return "mock-user-id"; // 여기서는 임시로 mock 처리
    }
}