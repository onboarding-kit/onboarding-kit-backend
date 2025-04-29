package com.api.onboardingkit.member.controller;

import com.api.onboardingkit.config.response.dto.CustomResponse;
import com.api.onboardingkit.config.response.dto.SuccessStatus;
import com.api.onboardingkit.config.exception.CustomException;
import com.api.onboardingkit.config.exception.ErrorCode;
import com.api.onboardingkit.member.dto.MemberRequestDto;
import com.api.onboardingkit.member.entity.Member;
import com.api.onboardingkit.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/member/me/{email}")
    public CustomResponse<Member> getMemberByEmail(@PathVariable String email) {
        Member mem = memberService.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND_FROM_EMAIL));
        return CustomResponse.success(mem, SuccessStatus.GET_MEMBER_BY_EMAIL);
    }

    @PatchMapping("/member/me")
    public CustomResponse<?> UpdateMemberInfo(@Valid @RequestBody MemberRequestDto requestDto){
        memberService.saveOrUpdate(requestDto);
        return CustomResponse.success(SuccessStatus.UPDATE_MEMBER_OK);
    }

}
