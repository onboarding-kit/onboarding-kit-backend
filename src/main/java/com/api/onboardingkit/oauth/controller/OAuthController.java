package com.api.onboardingkit.oauth.controller;

import com.api.onboardingkit.config.AbstractService;
import com.api.onboardingkit.config.response.dto.CustomResponse;
import com.api.onboardingkit.config.response.dto.SuccessStatus;
import com.api.onboardingkit.oauth.dto.OAuthRequestDto;
import com.api.onboardingkit.oauth.dto.OAuthResponseDto;
import com.api.onboardingkit.oauth.dto.TokenReissueRequestDto;
import com.api.onboardingkit.oauth.dto.TokenResponseDto;
import com.api.onboardingkit.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<OAuthResponseDto>> OAuth2Login(@RequestBody OAuthRequestDto requestDto) {
        OAuthResponseDto responseDto = oAuthService.authenticate(requestDto);
        return ResponseEntity.ok(CustomResponse.success(responseDto, SuccessStatus.OAUTH_AUTHENTICATION));
    }

    @PostMapping("/reissue")
    public ResponseEntity<CustomResponse<TokenResponseDto>> reissue(@RequestBody TokenReissueRequestDto requestDto) {
        TokenResponseDto response = oAuthService.reissueToken(requestDto);
        return ResponseEntity.ok(CustomResponse.success(response, SuccessStatus.TOKEN_REISSUE_OK));
    }

    @PostMapping("/logout")
    public ResponseEntity<CustomResponse<String>> logout() {
        oAuthService.logout();
        return ResponseEntity.ok(CustomResponse.success("로그아웃 성공입니다.", SuccessStatus.LOGOUT_OK));
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<CustomResponse<String>> withdraw() {
        oAuthService.withdraw();
        return ResponseEntity.ok(CustomResponse.success("회원탈퇴 성공입니다.", SuccessStatus.MEMBER_WITHDRAW_OK));
    }
}
