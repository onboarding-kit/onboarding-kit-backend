package com.api.onboardingkit.global.response.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResonDto {
    private String code;
    private String message;
}
