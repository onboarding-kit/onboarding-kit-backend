package com.api.onboardingkit.member;

import com.api.onboardingkit.config.JwtAuthenticationFilter;
import com.api.onboardingkit.config.JwtTokenProvider;
import com.api.onboardingkit.member.controller.MemberController;
import com.api.onboardingkit.member.entity.Member;
import com.api.onboardingkit.member.entity.SocialType;
import com.api.onboardingkit.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
public class MemberControllerRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final String AUTH_HEADER = "Authorization";
    private final String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9...";

    @Test
    @DisplayName("내 프로필 조회 API")
    void getMyProfile() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .email("member@golfzon.com")
                .name("홍길동")
                .nickname("온보딩러")
                .role("백엔드")
                .detailRole("API개발")
                .experience(3)
                .socialType(SocialType.KAKAO)
                .socialId("kakao-112233")
                .build();

        given(memberService.getCurrentMember()).willReturn(Optional.of(member));

        mockMvc.perform(get("/member/me")
                        .header(AUTH_HEADER, BEARER_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-get-profile",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("회원 ID"),
                                fieldWithPath("data.email").description("이메일"),
                                fieldWithPath("data.name").description("소셜 사용자명"),
                                fieldWithPath("data.nickname").description("닉네임"),
                                fieldWithPath("data.role").description("직무 (ex. 백엔드)"),
                                fieldWithPath("data.detailRole").description("상세 직무"),
                                fieldWithPath("data.experience").description("연차 (숫자)"),
                                fieldWithPath("data.socialType").description("소셜 로그인 유형 (google, kakao, apple)"),
                                fieldWithPath("data.socialId").description("소셜 ID")
                        )));
    }

    @Test
    @DisplayName("내 프로필 수정 API")
    void updateMyProfile() throws Exception {
        mockMvc.perform(patch("/member")
                        .header(AUTH_HEADER, BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nickname": "온보딩러",
                                    "role": "백엔드",
                                    "detailRole": "API개발",
                                    "experience": 3
                                }
                                """))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-update-profile",
                        requestFields(
                                fieldWithPath("nickname").description("닉네임 (8자 이하, 한글/영문만 허용)"),
                                fieldWithPath("role").description("직군 (예: 백엔드, 프론트, 기획 등)"),
                                fieldWithPath("detailRole").description("상세 직무"),
                                fieldWithPath("experience").description("연차 (숫자)")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("결과 메시지")
                        )));
    }
}
