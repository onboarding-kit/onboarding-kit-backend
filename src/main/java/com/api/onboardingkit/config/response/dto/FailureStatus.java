package com.api.onboardingkit.config.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FailureStatus {
    FAIL(500, "실패"),

    // OAuth 관련
    OAUTH_AUTHENTICATION_FAIL(401, "소셜 액세스 토큰 인증 실패"),
    TOKEN_REISSUE_FAIL(401, "토큰 재발급 실패"),
    LOGOUT_FAIL(500, "로그아웃 실패"),
    MEMBER_WITHDRAW_FAIL(500, "회원탈퇴 실패"),

    // Member 관련
    UPDATE_MEMBER_FAIL(400, "회원 정보 등록 실패");

    private final int code;
    private final String message;

}
