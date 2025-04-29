package com.api.onboardingkit.member.controller;

import ch.qos.logback.core.status.ErrorStatus;
import com.api.onboardingkit.config.SecurityUtil;
import com.api.onboardingkit.global.response.dto.Response;
import com.api.onboardingkit.global.response.dto.SuccessStatus;
import com.api.onboardingkit.global.response.exception.CustomException;
import com.api.onboardingkit.global.response.exception.ErrorCode;
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
    public Response<Member> getMemberByEmail(@PathVariable String email) {
        Member mem = memberService.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND_FROM_EMAIL));
        return Response.success(mem, SuccessStatus.GET_MEMBER_BY_EMAIL);
    }

    @PatchMapping("/member/me")
    public Response<?> UpdateMemberInfo(@Valid @RequestBody MemberRequestDto requestDto){
        memberService.saveOrUpdate(requestDto);
        return Response.success(SuccessStatus.UPDATE_MEMBER_OK);
    }

}
