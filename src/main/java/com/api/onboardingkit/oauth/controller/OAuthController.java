package com.api.onboardingkit.oauth.controller;

import com.api.onboardingkit.oauth.dto.OAuthRequestDto;
import com.api.onboardingkit.oauth.dto.OAuthResponseDto;
import com.api.onboardingkit.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/login")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping
    public OAuthResponseDto loginWithOAuth2(
            @RequestBody OAuthRequestDto request
    ) {
        return oAuthService.authenticate(request);
    }
}