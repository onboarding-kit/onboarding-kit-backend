package com.api.onboardingkit.member.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class MemberRequestDto {
    @Pattern(regexp = "^[가-힣a-zA-Z]{1,8}$", message = "닉네임은 8자 이하로 알파벳과 한글만 조합할 수 있어요.")
    private String nickname; // 닉네임
    private String role; // 디자이너, 개발자, 기획자
    private String detailRole; // 세부 직무
    @Pattern(regexp = "^[0-9]+$", message = "숫자만 입력해주세요.")
    private int experience; // 연차
}
