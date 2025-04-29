package com.api.onboardingkit.oauth.controller;

import com.api.onboardingkit.config.response.dto.CustomResponse;
import com.api.onboardingkit.oauth.dto.OAuthRequestDto;
import com.api.onboardingkit.oauth.dto.OAuthResponseDto;
import com.api.onboardingkit.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping("/login")
    public CustomResponse<OAuthResponseDto> OAuth2Login(@RequestBody OAuthRequestDto requestDto) {
        return oAuthService.authenticate(requestDto);
    }
}
