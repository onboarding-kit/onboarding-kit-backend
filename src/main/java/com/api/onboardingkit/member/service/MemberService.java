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
        Optional<Member> mem = memberRepository.findByEmail(member.getEmail());

        return mem.map(m ->{
            m.updateProfile(member.getNickname(), member.getRole(), member.getDetailRole(), member.getExperience());
            return memberRepository.save(m);
        }).orElseGet(() -> memberRepository.save(member));
    }

    public Optional<Member> findByEmail(String email) {return memberRepository.findByEmail(email);}
}
