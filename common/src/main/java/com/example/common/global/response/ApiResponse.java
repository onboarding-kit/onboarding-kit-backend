package com.example.common.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;

    //성공적인 응답 (데이터가 있을 때)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("200", "Success", data);
    }

    //성공적인 응답 (데이터가 없을 때)
    public static ApiResponse<Void> success() {
        return new ApiResponse<>("200", "Success", null);
    }

    //특정 코드로 성공적인 응답
    public static ApiResponse<Void> success(String code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    //실패한 응답 (메시지만 있을 때)
    public static ApiResponse<Void> fail(String message) {
        return new ApiResponse<>("500", message, null);
    }

    //실패한 응답 (상태 코드와 메시지 제공)
    public static ApiResponse<Void> fail(String code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
