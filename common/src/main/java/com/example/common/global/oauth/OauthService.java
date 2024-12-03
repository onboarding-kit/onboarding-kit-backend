package com.example.common.global.oauth;

import com.example.common.global.exception.BaseException;
import com.example.common.global.exception.ErrorCode;
import com.example.common.global.oauth.dto.OauthRes;
import com.example.common.global.oauth.dto.UserSignupReq;
import com.example.common.global.oauth.dto.UserSignupRes;
import com.example.common.global.oauth.jwt.service.TokenProvider;
import com.example.common.global.oauth.kakao.dto.OauthParams;
import com.example.common.global.oauth.kakao.dto.OauthUser;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OauthService {
    private final RequestOauthInfoService requestOauthInfoService;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public OauthRes getUserByOauthLogin(OauthParams oauthParams) {
        log.info("----- Oauth Login 시도 -----");

        //인증 파라미터 객체를 이용하여 해당 enum 클래스에 해당하는 메소드 수행
        OauthUser request = requestOauthInfoService.request(oauthParams);

        //기존 유저인지 체크
        Optional<User> bySocialId = userRepository.findBySocialId(request.getSocialId());

        //회원이 아닐 때
        if (bySocialId.isEmpty()) {
            log.error("신입키트 회원이 아닙니다.");
            throw new BaseException(ErrorCode.USER_NOT_REGISTERED);
        }

        User user = bySocialId.get();
        String accessToken = tokenProvider.issueAccessToken(user.getId());
        String refreshToken = tokenProvider.issueRefreshToken(user.getId());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("AccessToken", accessToken);
        httpHeaders.add("RefreshToken", refreshToken);

        return new OauthRes(httpHeaders, user.getId());
    }

    public UserSignupRes signup(UserSignupReq userSignupReq) {
        User user = User.builder()
                .socialId(userSignupReq.socialId())
                .oauthProvider(userSignupReq.oauthProvider())
                .nickname(userSignupReq.nickname())
                .job(userSignupReq.job())
                .workExperience(userSignupReq.workExperience())
                .build();
        User save = userRepository.save(user);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("AccessToken", "Bearer " + tokenProvider.issueAccessToken(save.getId()));
        httpHeaders.add("RefreshToken", "Bearer "+tokenProvider.issueRefreshToken(save.getId()));

        return new UserSignupRes(save.getId(), httpHeaders);
    }


}
