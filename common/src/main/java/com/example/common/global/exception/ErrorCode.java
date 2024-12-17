package com.example.common.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //유저 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionMessage.USER_NOT_FOUND),
    USER_NOT_REGISTERED(HttpStatus.BAD_REQUEST, ExceptionMessage.USER_NOT_REGISTERED),

    //아티클 관련

    //체크리스트 관련
    CHECKLIST_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionMessage.CHECKLIST_NOT_FOUND),

    //폴더 관련
    FOLDER_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionMessage.FOLDER_NOT_FOUND),

    //토큰 관련
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, ExceptionMessage.TOKEN_NOT_FOUND);


    private final HttpStatus status;
    private final String message;
    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
