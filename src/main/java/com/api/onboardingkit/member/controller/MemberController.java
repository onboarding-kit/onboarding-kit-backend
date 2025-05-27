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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<CustomResponse<Member>> getMyProfile() {
        Member member = memberService.getCurrentMember()
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND_FROM_ACCESS_TOKEN));
        return ResponseEntity.ok(CustomResponse.success(member, SuccessStatus.GET_MEMBER_OK));
    }

    @PatchMapping
    public ResponseEntity<CustomResponse<?>> updateMyProfile(@Valid @RequestBody MemberRequestDto requestDto){
        memberService.saveOrUpdate(requestDto);
        return ResponseEntity.ok(CustomResponse.success(SuccessStatus.UPDATE_MEMBER_OK));
    }

}
