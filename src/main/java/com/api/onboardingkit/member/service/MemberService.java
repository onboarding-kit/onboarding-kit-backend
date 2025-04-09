package com.api.onboardingkit.member.service;

import com.api.onboardingkit.member.entity.Member;
import com.api.onboardingkit.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member saveOrUpdate(Member member) {
        Optional<Member> optionalMember = memberRepository.findByEmail(member.getEmail());

        return optionalMember.map(mem ->{
            mem.updateProfile(member.getNickname(), member.getRole(), member.getDetailRole(), member.getExperience());
            return memberRepository.save(mem);
        }).orElseGet(() -> memberRepository.save(member));
    }

    public Optional<Member> findByEmail(String email) {return memberRepository.findByEmail(email);}


}
