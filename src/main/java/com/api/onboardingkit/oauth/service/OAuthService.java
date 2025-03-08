package com.api.onboardingkit.oauth.service;

import com.api.onboardingkit.oauth.dto.OAuthRequestDto;
import com.api.onboardingkit.oauth.dto.OAuthResponseDto;
import com.api.onboardingkit.oauth.provider.OAuthProvider;
import com.api.onboardingkit.oauth.provider.OAuthProviderFactory;
import com.api.onboardingkit.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthProviderFactory oAuthProviderFactory;
    private final JwtTokenProvider jwtTokenProvider;

    public OAuthResponseDto authenticate(OAuthRequestDto request) {
        // OAuthProvider 선택 (Google/Kakao/Apple)
        OAuthProvider provider = oAuthProviderFactory.getProvider(request.getProvider());

        // Access Token 검증 및 사용자 ID 반환
        String socialUserId = provider.validateToken(request.getToken());

        // JWT 발급
        String accessToken = jwtTokenProvider.generateToken(socialUserId);
        String refreshToken = jwtTokenProvider.generateRefreshToken(socialUserId);

        return new OAuthResponseDto(accessToken, refreshToken);
    }
}