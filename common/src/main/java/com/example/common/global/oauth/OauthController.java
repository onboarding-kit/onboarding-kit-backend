package com.example.common.global.oauth;

import com.example.common.global.oauth.dto.OauthReq;
import com.example.common.global.oauth.dto.OauthRes;
import com.example.common.global.oauth.dto.UserSignupReq;
import com.example.common.global.oauth.dto.UserSignupRes;
import com.example.common.global.oauth.kakao.dto.KakaoParams;
import com.example.common.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth")
public class OauthController {
    private final OauthService oauthService;
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> socialLogin(@Valid @RequestBody OauthReq oauthReq) {
        OauthRes oauthRes = switch (oauthReq.oauthProvider()) {
            case KAKAO -> {
                log.info("KAKAO 로그인 요청");
                yield oauthService.getUserByOauthLogin(new KakaoParams(oauthReq.code()));
            }
            case APPLE -> {
                log.info("APPLE 로그인 요청");
                yield null;
            }
        };

        return ResponseEntity.ok()
                .headers(oauthRes.httpHeaders())
                .body(ApiResponse.success(oauthRes.userId()));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(@Valid @RequestBody UserSignupReq userSignupReq) {
        UserSignupRes signup = oauthService.signup(userSignupReq);

        return ResponseEntity.ok()
                .headers(signup.httpHeaders())
                .body(ApiResponse.success(signup.userId()));
    }
}
