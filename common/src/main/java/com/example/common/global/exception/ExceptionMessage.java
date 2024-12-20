package com.example.common.global.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExceptionMessage {
    //유저 관련
    public static final String USER_NOT_FOUND = "해당 유저가 존재 하지 않습니다.";
    public static final String USER_NOT_REGISTERED = "등록된 회원이 아닙니다.";

    //S3 관련
    public static final String S3_BAD_REQUEST = "S3 잘못된 요청입니다";

    //토큰 관련
    public static final String TOKEN_VERIFICATION = "해당 토큰이 유효하지 않습니다.";
    public static final String TOKEN_NOT_FOUND = "해당 토큰이 존재 하지 않습니다.";
    public static final String SOCIAL_TOKEN_RECEIVE_FAILED = "소셜 로그인 서버로부터 토큰을 수신하지 못했습니다.";
}
