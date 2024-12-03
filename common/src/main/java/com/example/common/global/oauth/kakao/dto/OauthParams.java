package com.example.common.global.oauth.kakao.dto;

import org.springframework.util.MultiValueMap;

public interface OauthParams {
    OauthProvider oauthProvider();
    String getAuthorizationCode();
    MultiValueMap<String, String> makeBody();
}
