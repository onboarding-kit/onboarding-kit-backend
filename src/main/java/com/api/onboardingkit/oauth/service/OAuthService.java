package com.api.onboardingkit.oauth.service;

import com.api.onboardingkit.config.AbstractService;
import com.api.onboardingkit.config.JwtTokenProvider;
import com.api.onboardingkit.config.exception.CustomException;
import com.api.onboardingkit.config.exception.ErrorCode;
import com.api.onboardingkit.member.entity.Member;
import com.api.onboardingkit.member.entity.SocialType;
import com.api.onboardingkit.member.repository.MemberRepository;
import com.api.onboardingkit.oauth.dto.*;
import com.api.onboardingkit.oauth.provider.OAuthProvider;
import com.api.onboardingkit.oauth.provider.OAuthProviderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class OAuthService extends AbstractService {
    private final OAuthProviderFactory oAuthProviderFactory;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public OAuthResponseDto authenticate(OAuthRequestDto oAuthRequestDto) {
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
        String accessToken = jwtTokenProvider.generateToken(member.getId(), socialType);
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getId(), socialType);

        member.setRefreshToken(refreshToken);
        memberRepository.save(member);

        return new OAuthResponseDto(accessToken, refreshToken);
    }

    public TokenResponseDto reissueToken(TokenReissueRequestDto requestDto) {

        // refresh 토큰 유효성 확인
        if (!jwtTokenProvider.validateToken(requestDto.getRefreshToken())) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // claims에서 memberId 추출
        Long memberId = Long.valueOf(jwtTokenProvider.getMemberIdFromToken(requestDto.getRefreshToken()));

        // DB에 저장된 refresh 토큰과 비교
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (!requestDto.getRefreshToken().equals(member.getRefreshToken())) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        // 새 토큰 발급 및 저장
        String newAccessToken = jwtTokenProvider.generateToken(member.getId(), member.getSocialType());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(member.getId(), member.getSocialType());
        member.setRefreshToken(newRefreshToken);
        memberRepository.save(member);

        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout() {
        Long memberId = getMemberId();
        System.out.println(memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.setRefreshToken(null);
        memberRepository.save(member);
    }

    @Transactional
    public void withdraw() {
        Long memberId=getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        memberRepository.delete(member);
    }
}
