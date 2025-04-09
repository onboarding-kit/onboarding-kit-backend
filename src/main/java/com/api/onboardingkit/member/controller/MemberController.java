package com.api.onboardingkit.member.controller;

import ch.qos.logback.core.status.ErrorStatus;
import com.api.onboardingkit.global.response.dto.Response;
import com.api.onboardingkit.global.response.dto.SuccessStatus;
import com.api.onboardingkit.global.response.exception.CustomException;
import com.api.onboardingkit.global.response.exception.ErrorCode;
import com.api.onboardingkit.member.entity.Member;
import com.api.onboardingkit.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/member/me/{email}")
    public Response<?> getMemberByEmail(@PathVariable String email) {
        Member mem = memberService.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        return Response.success(mem, SuccessStatus.GET_MEMBER_BY_EMAIL);
    }

}
