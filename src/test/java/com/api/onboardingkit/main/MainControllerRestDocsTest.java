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

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
        MainStatusChecklistDTO dto = new MainStatusChecklistDTO(1L, "입사 준비", 5, 3, 60.0);

        given(mainService.getMainChecklistStatus()).willReturn(List.of(dto));

        mockMvc.perform(get("/main/checklists/status")
                        .header(AUTH_HEADER, BEARER_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("main-checklists-status",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].checklistId").description("체크리스트 ID"),
                                fieldWithPath("data[].title").description("체크리스트 제목"),
                                fieldWithPath("data[].totalItems").description("전체 항목 수"),
                                fieldWithPath("data[].completedItems").description("완료 항목 수"),
                                fieldWithPath("data[].progress").description("진행률 (%)")
                        )));
    }

    @DisplayName("메인 체크리스트 상세 조회 API - 특정 ID로 조회")
    @Test
    void getMainChecklist_byId() throws Exception {
        long checklistId = 1L;

        MainChecklistDTO dto = new MainChecklistDTO(
                checklistId,
                "입사 준비",
                List.of(new MainChecklistItemDTO(10L, "노트북 세팅", true, LocalDateTime.now()))
        );

        given(mainService.getMainChecklist(checklistId)).willReturn(dto);

        mockMvc.perform(get("/main/checklists/{checklistId}", checklistId)
                        .header(AUTH_HEADER, BEARER_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("main-checklists-detail",
                        requestHeaders(
                                headerWithName(AUTH_HEADER).description("Bearer 인증 토큰")
                        ),
                        pathParameters(
                                parameterWithName("checklistId").description("체크리스트 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.checklistId").description("체크리스트 ID"),
                                fieldWithPath("data.checklistTitle").description("체크리스트 제목"),
                                fieldWithPath("data.checklistItems[].id").description("체크리스트 항목 ID"),
                                fieldWithPath("data.checklistItems[].content").description("항목 내용"),
                                fieldWithPath("data.checklistItems[].completed").description("완료 여부 (boolean)"),
                                fieldWithPath("data.checklistItems[].createdTime").description("생성 일시")
                        )
                ));

        then(mainService).should().getMainChecklist(checklistId);
    }

    @DisplayName("메인 체크리스트 상세 조회 API - 파라미터 없으면 최신 체크리스트")
    @Test
    void getMainChecklist_recentWhenNoId() throws Exception {
        MainChecklistDTO dto = new MainChecklistDTO(
                2L,
                "온보딩 체크리스트",
                List.of(new MainChecklistItemDTO(20L, "보안 교육 수강", false, LocalDateTime.now()))
        );

        // null 인자 매칭은 isNull() 사용
        given(mainService.getMainChecklist(isNull())).willReturn(dto);

        mockMvc.perform(get("/main/checklists")
                        .header(AUTH_HEADER, BEARER_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("main-checklists-recent",
                        requestHeaders(
                                headerWithName(AUTH_HEADER).description("Bearer 인증 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.checklistId").description("체크리스트 ID"),
                                fieldWithPath("data.checklistTitle").description("체크리스트 제목"),
                                fieldWithPath("data.checklistItems[].id").description("체크리스트 항목 ID"),
                                fieldWithPath("data.checklistItems[].content").description("항목 내용"),
                                fieldWithPath("data.checklistItems[].completed").description("완료 여부 (boolean)"),
                                fieldWithPath("data.checklistItems[].createdTime").description("생성 일시")
                        )
                ));

        then(mainService).should().getMainChecklist(isNull());
    }

    @Test
    @DisplayName("메인 아티클 목록 조회 API")
    void getMainArticles() throws Exception {
        MainArticleDTO dto = new MainArticleDTO(
                "첫 출근 꿀팁",
                "이 글은 첫 출근을 앞둔 당신에게 도움이 될 것입니다.",
                "thumb.jpg",
                "https://example.com",
                456,
                "https://example.com",
                "백엔드",
                LocalDateTime.parse("2024-03-01T10:00:00")
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
                                fieldWithPath("data[].views").description("조회수"),
                                fieldWithPath("data[].source").description("출처"),
                                fieldWithPath("data[].categoryName").description("키테고리"),
                                fieldWithPath("data[].postDate").description("게시일")
                        )));
    }
}
