package com.api.onboardingkit.oauth;

import com.api.onboardingkit.config.JwtAuthenticationFilter;
import com.api.onboardingkit.config.JwtTokenProvider;
import com.api.onboardingkit.config.response.dto.CustomResponse;
import com.api.onboardingkit.config.response.dto.SuccessStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.api.onboardingkit.oauth.controller.OAuthController;
import com.api.onboardingkit.oauth.dto.OAuthRequestDto;
import com.api.onboardingkit.oauth.dto.OAuthResponseDto;
import com.api.onboardingkit.oauth.service.OAuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

@WebMvcTest(OAuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
public class OauthControllerRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OAuthService oAuthService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("OAuth 로그인 API")
    void oauthLogin() throws Exception {
        // given
        OAuthResponseDto responseDto = new OAuthResponseDto("access-token", "refresh-token");
        given(oAuthService.authenticate(any(OAuthRequestDto.class)))
                .willReturn(CustomResponse.success(responseDto, SuccessStatus.OAUTH_AUTHENTICATION));

        // when
        mockMvc.perform(post("/oauth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "socialType": "kakao",
                                "token": "kakao-access-token"
                            }
                        """))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("oauth-login",
                        requestFields(
                                fieldWithPath("socialType").description("소셜 로그인 유형 (google, kakao, apple)"),
                                fieldWithPath("token").description("소셜 로그인 access token")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.accessToken").description("JWT access token"),
                                fieldWithPath("data.refreshToken").description("JWT refresh token"),
                                fieldWithPath("data.tokenType").description("토큰 타입 (항상 'Bearer')")
                        )
                ));
    }
}
