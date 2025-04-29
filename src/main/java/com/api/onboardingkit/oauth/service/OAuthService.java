package com.api.onboardingkit.oauth.service;

import com.api.onboardingkit.config.JwtTokenProvider;
import com.api.onboardingkit.config.response.dto.CustomResponse;
import com.api.onboardingkit.config.response.dto.SuccessStatus;
import com.api.onboardingkit.member.entity.Member;
import com.api.onboardingkit.member.entity.SocialType;
import com.api.onboardingkit.member.repository.MemberRepository;
import com.api.onboardingkit.oauth.dto.OAuthRequestDto;
import com.api.onboardingkit.oauth.dto.OAuthResponseDto;
import com.api.onboardingkit.oauth.dto.SocialUserInfo;
import com.api.onboardingkit.oauth.provider.OAuthProvider;
import com.api.onboardingkit.oauth.provider.OAuthProviderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OAuthService {
    private final OAuthProviderFactory oAuthProviderFactory;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public CustomResponse<OAuthResponseDto> authenticate(OAuthRequestDto oAuthRequestDto) {
        // 요청받은 OAuth Provider 추출
        OAuthProvider provider = oAuthProviderFactory.getProvider(oAuthRequestDto.getSocialType());

        // 소셜 계정의 Access Token 검증 및 사용자 ID, email 반환
        SocialUserInfo memberInfo = provider.validateToken(oAuthRequestDto.getToken());

        // 사용자 조회 or DB에 저장
        SocialType socialType = SocialType.from(oAuthRequestDto.getSocialType());
        Member member = memberRepository.findBySocialIdAndSocialType(memberInfo.getSocialId(), socialType)
                .orElseGet(()-> {
                    Member newMember = Member.builder()
                            .socialType(socialType)
                            .socialId(memberInfo.getSocialId())
                            .email(memberInfo.getEmail())
                            .experience(0)
                            .build();
                    return memberRepository.save(newMember);
                });

        // 응답할 JWT 토큰 발급
        String accessToken = jwtTokenProvider.generateToken(memberInfo.getSocialId(), socialType.getValue());
        String refreshToken = jwtTokenProvider.generateRefreshToken(memberInfo.getSocialId());

        return CustomResponse.success(
                new OAuthResponseDto(accessToken, refreshToken),
                SuccessStatus.OAUTH_AUTHENTICATION
        );
    }
}
