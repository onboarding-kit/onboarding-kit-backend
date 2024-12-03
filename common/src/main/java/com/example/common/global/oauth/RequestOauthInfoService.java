package com.example.common.global.oauth;

import com.example.common.global.oauth.kakao.client.OauthClient;
import com.example.common.global.oauth.kakao.dto.OauthParams;
import com.example.common.global.oauth.kakao.dto.OauthProvider;
import com.example.common.global.oauth.kakao.dto.OauthUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RequestOauthInfoService {
    private final Map<OauthProvider, OauthClient> clients;

    public RequestOauthInfoService(List<OauthClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OauthClient::oauthProvider, Function.identity()));
    }

    public OauthUser request(OauthParams oauthParams) {
        //oauth provider 요청에 맞는 client get
        OauthClient client = clients.get(oauthParams.oauthProvider());
        //client에서 accessToken get 로직 실행
        String accessToken = client.getOauthLoginToken(oauthParams);
        //accessToken으로 user info get
        return client.getUserInfo(accessToken);
    }
}
