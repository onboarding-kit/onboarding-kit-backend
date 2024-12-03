package com.example.common.global.oauth.dto;

import com.example.common.global.oauth.kakao.dto.OauthProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserSignupReq (
        @NotBlank
        String socialId,
        @NotNull
        OauthProvider oauthProvider,
        @NotBlank
        String nickname,
        @NotNull
        String job,
        int workExperience
) {
}
