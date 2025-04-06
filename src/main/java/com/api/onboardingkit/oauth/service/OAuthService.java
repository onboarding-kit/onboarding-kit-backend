package com.api.onboardingkit.oauth.service;

import com.api.onboardingkit.config.JwtTokenProvider;
import com.api.onboardingkit.global.response.dto.Response;
import com.api.onboardingkit.global.response.dto.SuccessStatus;
import com.api.onboardingkit.oauth.dto.OAuthRequestDto;
import com.api.onboardingkit.oauth.dto.OAuthResponseDto;
import com.api.onboardingkit.oauth.provider.OAuthProvider;
import com.api.onboardingkit.oauth.provider.OAuthProviderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final OAuthProviderFactory oAuthProviderFactory;
    private final JwtTokenProvider jwtTokenProvider;

    public Response<OAuthResponseDto> authenticate(OAuthRequestDto oAuthRequestDto) {
        // 요청받은 OAuth Provider 추출
        OAuthProvider provider = oAuthProviderFactory.getProvider(oAuthRequestDto.getProvider());

        // 소셜 계정의 Access Token 검증 및 사용자 ID 반환
        String socialUserId = provider.validateToken(oAuthRequestDto.getToken());

        // 응답할 JWT 토큰 발급
        String accessToken = jwtTokenProvider.generateToken(socialUserId);
        String refreshToken = jwtTokenProvider.generateToken(socialUserId);

        return Response.success(
                new OAuthResponseDto(accessToken, refreshToken),
                SuccessStatus.OAUTH_AUTHENTICATION
        );
    }
}
