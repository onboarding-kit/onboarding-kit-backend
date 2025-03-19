package com.api.onboardingkit.global.response.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    TEST_ERROR_CODE("400", "응답 테스트 실패입니다.");

    private final String code;
    private final String message;
}
