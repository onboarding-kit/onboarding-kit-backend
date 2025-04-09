package com.api.onboardingkit.global.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus {
    TEST_SUCCESS_CODE("200", "응답 테스트 성공입니다."),

    //oauth
    OAUTH_AUTHENTICATION("200", "OAuthService authebtication 성공"),

    //member
    GET_MEMBER_BY_EMAIL("200","이메일로 멤버 객체 추출 성공"),
    ;

    private final String code;
    private final String message;
}
