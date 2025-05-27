package com.api.onboardingkit.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    TEST_ERROR_CODE(400, "응답 테스트 실패입니다."),

    //oauth
    NOT_ALLOWED_OAUTH_PROVIDER(405, "지원하지 않는 OAuth Provider 입니다."),
    NOT_FOUND_JWT_SECRET(404,"Jwt Secret 키를 찾을 수 없습니다."),
    TOKEN_VALIDATE_FAILED(400,"Jwt Token이 유효하지 않습니다."),
    APPLE_ID_VALIDATE_FAILED(400,"Apple ID 토큰이 유효하지 않습니다."),
    NOT_FOUND_APPLE_PUBLIC_KEY(404,"Apple Public Key를 찾을 수 없습니다."),
    KAKAO_ID_VALIDATE_FAILED(400,"Kakao 액세스 토큰이 유효하지 않습니다."),
    GOOGLE_ID_VALIDATE_FAILED(400,"Google 액세스 토큰이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN(400,"유효하지 않은 refresh 토큰입니다."),
    REFRESH_TOKEN_MISMATCH(400,"멤버에 저장된 refresh 토큰과 일치하지 않습니다."),

    //member
    MEMBER_NOT_FOUND_FROM_EMAIL(404,"해당 이메일의 멤버를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND_FROM_ACCESS_TOKEN(404,"해당 액세스 토큰으로 멤버를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(404,"회원 정보를 찾을 수 없습니다."),
    ;

    private final int code;
    private final String message;
}
