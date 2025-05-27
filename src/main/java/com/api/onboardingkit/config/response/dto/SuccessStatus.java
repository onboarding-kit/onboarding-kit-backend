package com.api.onboardingkit.config.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus {
    TEST_SUCCESS_CODE(200, "응답 테스트 성공입니다."),

    SUCCESS(200,"성공"),
    //oauth
    OAUTH_AUTHENTICATION(200, "소셜 액세스 토큰 인증 성공"),
    TOKEN_REISSUE_OK(200,"토큰 재발급 성공"),

    //member
    GET_MEMBER_OK(200,"멤버 조회 성공"),
    UPDATE_MEMBER_OK(200,"회원 정보가 등록되었습니다.");

    private final int code;
    private final String message;
}
