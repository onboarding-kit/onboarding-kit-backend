package com.api.onboardingkit.main;

import com.api.onboardingkit.config.JwtAuthenticationFilter;
import com.api.onboardingkit.config.JwtTokenProvider;
import com.api.onboardingkit.main.controller.MainController;
import com.api.onboardingkit.main.dto.MainArticleDTO;
import com.api.onboardingkit.main.dto.MainChecklistDTO;
import com.api.onboardingkit.main.dto.MainChecklistItemDTO;
import com.api.onboardingkit.main.dto.MainStatusChecklistDTO;
import com.api.onboardingkit.main.service.MainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
public class MainControllerRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MainService mainService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final String AUTH_HEADER = "Authorization";
    private final String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ2NzUwMzkyLCJleHAiOjE3NDY3NTIxOTJ9.at_CI6S-_tsBBbuncKyZeSCFDHV5PetEgi0MVHv7IjQ";

    @Test
    @DisplayName("메인 체크리스트 진행률 조회 API")
    void getChecklistProgress() throws Exception {
        MainStatusChecklistDTO dto = new MainStatusChecklistDTO("입사 준비", 5, 3, 60.0);

        given(mainService.getMainChecklistStatus()).willReturn(List.of(dto));

        mockMvc.perform(get("/main/checklists/status")
                        .header(AUTH_HEADER, BEARER_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("main-checklists-status",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].title").description("체크리스트 제목"),
                                fieldWithPath("data[].totalItems").description("전체 항목 수"),
                                fieldWithPath("data[].completedItems").description("완료 항목 수"),
                                fieldWithPath("data[].progress").description("진행률 (%)")
                        )));
    }

    @Test
    @DisplayName("메인 체크리스트 상세 조회 API")
    void getMainChecklist() throws Exception {
        MainChecklistDTO dto = new MainChecklistDTO(
                1L,
                "입사 준비",
                List.of(new MainChecklistItemDTO(10L, "노트북 세팅", true, LocalDateTime.now()))
        );

        given(mainService.getMainChecklist()).willReturn(dto);

        mockMvc.perform(get("/main/checklists")
                        .header(AUTH_HEADER, BEARER_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("main-checklists-detail",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.checklistId").description("체크리스트 ID"),
                                fieldWithPath("data.checklistTitle").description("체크리스트 제목"),
                                fieldWithPath("data.checklistItems[].id").description("체크리스트 항목 ID"),
                                fieldWithPath("data.checklistItems[].content").description("항목 내용"),
                                fieldWithPath("data.checklistItems[].completed").description(true),
                                fieldWithPath("data.checklistItems[].createdTime").description("생성일시")
                        )));
    }

    @Test
    @DisplayName("메인 아티클 목록 조회 API")
    void getMainArticles() throws Exception {
        MainArticleDTO dto = new MainArticleDTO(
                "첫 출근 꿀팁",
                "이 글은 첫 출근을 앞둔 당신에게 도움이 될 것입니다.",
                "thumb.jpg",
                "https://example.com",
                456
        );

        given(mainService.getMainArticles()).willReturn(List.of(dto));

        mockMvc.perform(get("/main/articles")
                        .header(AUTH_HEADER, BEARER_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("main-articles",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].title").description("아티클 제목"),
                                fieldWithPath("data[].summary").description("요약"),
                                fieldWithPath("data[].thumbnail").description("썸네일 이미지 경로"),
                                fieldWithPath("data[].url").description("링크 URL"),
                                fieldWithPath("data[].views").description("조회수")
                        )));
    }
}
