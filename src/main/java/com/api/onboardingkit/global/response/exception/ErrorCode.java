package com.api.onboardingkit.global.response.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    TEST_ERROR_CODE("400", "응답 테스트 실패입니다."),

    //oauth
    NOT_ALLOWED_OAUTH_PROVIDER("405", "지원하지 않는 OAuth Provider 입니다."),
    NOT_FOUND_JWT_SECRET("404","Jwt Secret 키를 찾을 수 없습니다."),
    TOKEN_VALIDATE_FAILED("400","Jwt Token이 유효하지 않습니다."),
    APPLE_ID_VALIDATE_FAILED("400","Apple ID 토큰이 유효하지 않습니다."),
    NOT_FOUND_APPLE_PUBLIC_KEY("404","Apple Public Key를 찾을 수 없습니다."),
    ;

    private final String code;
    private final String message;
}
