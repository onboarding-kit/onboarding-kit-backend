package com.example.common.global.oauth.dto;

import org.springframework.http.HttpHeaders;

public record UserSignupRes(
        Long userId,
        HttpHeaders httpHeaders
) {
}
