package com.api.onboardingkit.member.service;

import com.api.onboardingkit.config.AbstractService;
import com.api.onboardingkit.global.response.exception.CustomException;
import com.api.onboardingkit.global.response.exception.ErrorCode;
import com.api.onboardingkit.member.dto.MemberRequestDto;
import com.api.onboardingkit.member.entity.Member;
import com.api.onboardingkit.member.entity.SocialType;
import com.api.onboardingkit.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService extends AbstractService{
    private final MemberRepository memberRepository;

    @Transactional
    public Member saveOrUpdate(MemberRequestDto requestDto) {

        // 헤더에서 토큰으로 사용자 정보 추출하여 기능 수행
        SocialType socialType = SocialType.from(getSocialType());
        Optional<Member> optionalMember = memberRepository.findBySocialIdAndSocialType(getSocialId(), socialType);

        Member member = optionalMember
                .map(m -> {
                    m.updateProfile(
                            requestDto.getNickname(),
                            requestDto.getRole(),
                            requestDto.getDetailRole(),
                            requestDto.getExperience()
                    );
                    return memberRepository.save(m);
                })
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND_FROM_ACCESS_TOKEN));

        return member;
    }

    public Optional<Member> findByEmail(String email) {return memberRepository.findByEmail(email);}
}
