package com.example.common.global.oauth.kakao.client;

import com.example.common.global.oauth.kakao.dto.OauthUser;
import com.example.common.global.oauth.kakao.dto.OauthParams;
import com.example.common.global.oauth.kakao.dto.OauthProvider;

public interface OauthClient {
    OauthProvider oauthProvider();
    String getOauthLoginToken(OauthParams oauthParams);
    OauthUser getUserInfo(String accessToken);
}
