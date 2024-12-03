package com.example.common.global.oauth.kakao.dto;

public interface OauthUser {
    String getSocialId();

    String getEmail();

    String getNickname();

    OauthProvider getOauthProvider();
}
