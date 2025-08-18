package com.api.onboardingkit.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    TEST_ERROR_CODE(400, "응답 테스트 실패입니다."),

    //oauth
    NOT_ALLOWED_OAUTH_PROVIDER(405, "지원하지 않는 OAuth Provider 입니다."),
    APPLE_ID_VALIDATE_FAILED(400,"Apple ID 토큰이 유효하지 않습니다."),
    NOT_FOUND_APPLE_PUBLIC_KEY(404,"Apple Public Key를 찾을 수 없습니다."),
    KAKAO_ID_VALIDATE_FAILED(400,"Kakao 액세스 토큰이 유효하지 않습니다."),
    EXTERNAL_KAKAO_ERROR(502, "카카오 서버와 통신 중 문제가 발생했습니다."),
    GOOGLE_ID_VALIDATE_FAILED(400,"Google 액세스 토큰이 유효하지 않습니다."),

    // jwt
    TOKEN_VALIDATE_FAILED(400,"Jwt Token이 유효하지 않습니다."),
    NOT_FOUND_JWT_SECRET(404,"Jwt Secret 키를 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(400,"유효하지 않은 refresh 토큰입니다."),
    REFRESH_TOKEN_MISMATCH(400,"멤버에 저장된 refresh 토큰과 일치하지 않습니다."),
    JWT_EXPIRED(403,"토큰이 만료되었습니다."),
    JWT_PARSING_FAILED(403,"토큰 파싱 중 예외가 발생했습니다."),
    INVALID_JWT_TOKEN(401,"유효하지 않은 JWT 토큰입니다."),
    NOT_FOUND_AUTHORIZATION_HEADER(401, "Authorization 헤더가 없습니다."),
    NULL_POINT_HEADER_REQUEST(403,"헤더에 값이 없습니다."),

    // checklist
    NOT_FOUND_CHECKLIST(404, "체크리스트를 찾을 수 없습니다."),
    CHECKLIST_ACCESS_DENIED(403, "본인 소유의 체크리스트가 아닙니다."),

    //member
    MEMBER_NOT_FOUND_FROM_EMAIL(404,"해당 이메일의 멤버를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND_FROM_ACCESS_TOKEN(404,"해당 액세스 토큰으로 멤버를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(404,"회원 정보를 찾을 수 없습니다."),
    ;

    private final int code;
    private final String message;
}
