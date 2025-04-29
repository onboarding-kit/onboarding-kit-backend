package com.api.onboardingkit.main;

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
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class MainControllerRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MainService mainService;

    @Test
    @DisplayName("체크리스트 상태 조회 API")
    void getChecklistStatus() throws Exception {
        given(mainService.getMainChecklistStatus()).willReturn(List.of(
                new MainStatusChecklistDTO("온보딩 준비", 5, 3, 0.6)
        ));

        mockMvc.perform(get("/main/checklists/status")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("main-checklists-status",
                        responseFields(
                                fieldWithPath("[].title").description("체크리스트 제목"),
                                fieldWithPath("[].totalItems").description("전체 항목 수"),
                                fieldWithPath("[].completedItems").description("완료된 항목 수"),
                                fieldWithPath("[].progress").description("진행률 (0.0 ~ 1.0)")
                        )
                ));
    }

    @Test
    @DisplayName("전체 체크리스트 조회 API")
    void getChecklist() throws Exception {
        given(mainService.getMainChecklist()).willReturn(
                new MainChecklistDTO(
                        1L,
                        "온보딩 준비 체크리스트",
                        List.of(
                                new MainChecklistItemDTO(1L, "회사 이메일 세팅", LocalDateTime.of(2024, 3, 1, 10, 0)),
                                new MainChecklistItemDTO(2L, "Slack 가입", LocalDateTime.of(2024, 3, 2, 11, 0))
                        )
                )
        );

        mockMvc.perform(get("/main/checklists")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("main-checklists",
                        responseFields(
                                fieldWithPath("checklistId").description("체크리스트 ID"),
                                fieldWithPath("checklistTitle").description("체크리스트 제목"),
                                fieldWithPath("checklistItems[].id").description("체크리스트 항목 ID"),
                                fieldWithPath("checklistItems[].content").description("체크리스트 항목 내용"),
                                fieldWithPath("checklistItems[].createdTime").description("체크리스트 항목 생성 시간 (yyyy-MM-dd'T'HH:mm:ss)")
                        )
                ));
    }

    @Test
    @DisplayName("메인 아티클 리스트 조회 API")
    void getArticles() throws Exception {
        given(mainService.getMainArticles()).willReturn(List.of(
                new MainArticleDTO("첫 출근 꿀팁", "입사 첫날을 준비해보자!", "thumb1.jpg", "https://example.com/article1", 123),
                new MainArticleDTO("사내 커뮤니케이션 가이드", "효과적인 소통법", "thumb2.jpg", "https://example.com/article2", 456)
        ));

        mockMvc.perform(get("/main/articles")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("main-articles",
                        responseFields(
                                fieldWithPath("[].title").description("아티클 제목"),
                                fieldWithPath("[].summary").description("아티클 요약 설명"),
                                fieldWithPath("[].thumbnail").description("썸네일 파일명"),
                                fieldWithPath("[].url").description("아티클 링크 URL"),
                                fieldWithPath("[].views").description("조회수")
                        )
                ));
    }
}
