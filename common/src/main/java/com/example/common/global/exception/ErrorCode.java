package com.example.common.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //유저 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionMessage.USER_NOT_FOUND),
    //아티클 관련

    //체크리스트 관련

    //S3 관련
    FAILED_CONVERT_FILE(HttpStatus.BAD_REQUEST, ExceptionMessage.S3_BAD_REQUEST);

    private final HttpStatus status;
    private final String message;
    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
