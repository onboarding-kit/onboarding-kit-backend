package com.example.common.global.oauth.kakao.client;

import com.example.common.global.exception.BaseException;
import com.example.common.global.exception.ErrorCode;
import com.example.common.global.exception.ExceptionMessage;
import com.example.common.global.oauth.kakao.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class KakaoClient implements OauthClient{
    @Value("${oauth.kakao.token_url}")
    private String token_url;
    @Value("${oauth.kakao.user_url}")
    private String user_url;
    @Value("${oauth.kakao.grant_type}")
    private String grant_type;
    @Value("${oauth.kakao.client_id}")
    private String client_id;
    @Value("${oauth.kakao.client_secret}")
    private String client_secret;
    @Value("${oauth.kakao.redirect_uri}")
    private String redirect_uri;

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.KAKAO;
    }

    @Override
    public String getOauthLoginToken(OauthParams oauthParams) {
        String url = token_url;
        log.info("전달할 code: ${}", oauthParams.getAuthorizationCode());

        RestTemplate rt = new RestTemplate();
        //헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //바디 생성
        MultiValueMap<String, String> body = oauthParams.makeBody();
        body.add("grant_type", grant_type);
        body.add("client_id", client_id);
        body.add("client_secret", client_secret);
        body.add("redirect_uri", redirect_uri);

        //헤더+바디
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        log.info("current httpEntity state: ${}", tokenRequest);

        //토큰 수신
        KakaoToken kakaoToken = rt.postForObject(url, tokenRequest, KakaoToken.class);
        log.info("kakao accessToken: ${}", kakaoToken);

        if (kakaoToken == null) {
            log.error("token 수신 실패");
            throw new BaseException(ErrorCode.TOKEN_RECEIVE_FAILED);
        }

        return kakaoToken.getAccess_token();
    }

    @Override
    public OauthUser getUserInfo(String accessToken) {
        String url = user_url;
        log.info("receive token: ${}", accessToken);

        RestTemplate rt = new RestTemplate();

        //헤더 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("Authorization", "Bearer "+accessToken);

        //바디 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        //헤더+바디
        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(body, httpHeaders);

        return rt.postForObject(url, userInfoRequest, KakaoUser.class);
    }
}
