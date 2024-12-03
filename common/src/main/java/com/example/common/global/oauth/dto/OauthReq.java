package com.example.common.global.oauth.dto;

import com.example.common.global.oauth.kakao.dto.OauthProvider;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record OauthReq (
        @NotNull
        OauthProvider oauthProvider,
        @NotNull
        String code
)
{}
