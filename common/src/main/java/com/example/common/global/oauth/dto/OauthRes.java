package com.example.common.global.oauth.dto;

import org.springframework.http.HttpHeaders;

public record OauthRes (
        HttpHeaders httpHeaders,
        Long userId
) {
}
