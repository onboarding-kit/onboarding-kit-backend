package com.api.onboardingkit.config.response.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomResponse<T> {
    private final boolean isSuccess;
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    public static <T> CustomResponse<T> success(T result, SuccessStatus status) {
        return new CustomResponse<>(true, status.getCode(), status.getMessage(), result);
    }

    public static CustomResponse<Void> success(SuccessStatus status) {
        return new CustomResponse<>(true, status.getCode(), status.getMessage(), null);
    }

    public static CustomResponse<Void> failure(String code, String message) {
        return new CustomResponse<>(false, code, message, null);
    }
}
