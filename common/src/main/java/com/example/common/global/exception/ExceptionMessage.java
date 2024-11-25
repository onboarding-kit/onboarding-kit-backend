package com.example.common.global.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExceptionMessage {
    //유저 관련
    public static final String USER_NOT_FOUND = "해당 유저가 존재 하지 않습니다.";

    //S3 관련
    public static final String S3_BAD_REQUEST = "S3 잘못된 요청입니다";
}
