package com.api.onboardingkit.checklist;

import com.api.onboardingkit.checklist.controller.ChecklistController;
import com.api.onboardingkit.checklist.draft.ChecklistDraft;
import com.api.onboardingkit.checklist.dto.*;
import com.api.onboardingkit.checklist.service.ChecklistDraftService;
import com.api.onboardingkit.checklist.service.ChecklistService;
import com.api.onboardingkit.config.JwtAuthenticationFilter;
import com.api.onboardingkit.config.JwtTokenProvider;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ChecklistController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
public class ChecklistControllerRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChecklistService checklistService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private ChecklistDraftService checklistDraftService;

    private final String AUTH_HEADER = "Authorization";
    private final String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzQ2NzUwMzkyLCJleHAiOjE3NDY3NTIxOTJ9.at_CI6S-_tsBBbuncKyZeSCFDHV5PetEgi0MVHv7IjQ";

    @Test
    @DisplayName("체크리스트 전체 조회 API")
    void getUserChecklists() throws Exception {
        ChecklistResponseDTO response = ChecklistResponseDTO.builder()
                .id(1L)
                .userNo(100L)
                .title("입사 체크리스트")
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build();

        given(checklistService.getUserChecklists()).willReturn(List.of(response));

        mockMvc.perform(get("/checklists")
                        .header(AUTH_HEADER, BEARER_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("checklists-list",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].id").description("체크리스트 ID"),
                                fieldWithPath("data[].userNo").description("사용자 번호"),
                                fieldWithPath("data[].title").description("체크리스트 제목"),
                                fieldWithPath("data[].createdTime").description("생성일시"),
                                fieldWithPath("data[].updatedTime").description("수정일시")
                        )));
    }

    @Test
    @DisplayName("체크리스트 생성 API")
    void createChecklist() throws Exception {
        ChecklistResponseDTO response = ChecklistResponseDTO.builder()
                .id(1L)
                .userNo(100L)
                .title("신규 체크리스트")
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build();

        given(checklistService.createChecklist(any(ChecklistRequestDTO.class))).willReturn(response);

        mockMvc.perform(post("/checklists")
                        .header(AUTH_HEADER, BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "신규 체크리스트"
                                }
                                """))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("checklists-create",
                        requestFields(
                                fieldWithPath("title").description("체크리스트 제목")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("체크리스트 ID"),
                                fieldWithPath("data.userNo").description("사용자 번호"),
                                fieldWithPath("data.title").description("체크리스트 제목"),
                                fieldWithPath("data.createdTime").description("생성일시"),
                                fieldWithPath("data.updatedTime").description("수정일시")
                        )));
    }

    @Test
    @DisplayName("체크리스트 삭제 API")
    void deleteChecklist() throws Exception {
        mockMvc.perform(delete("/checklists/{checklistId}", 1L)
                        .header(AUTH_HEADER, BEARER_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("checklists-delete",
                        pathParameters(
                                parameterWithName("checklistId").description("삭제할 체크리스트 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("결과 메시지")
                        )));
    }

    @Test
    @DisplayName("체크리스트 제목 수정 API")
    void updateChecklistTitle() throws Exception {
        mockMvc.perform(put("/checklists/{checklistId}/title", 1L)
                        .header(AUTH_HEADER, BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "수정된 제목"
                                }
                                """))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("checklists-update-title",
                        pathParameters(
                                parameterWithName("checklistId").description("체크리스트 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("수정할 체크리스트 제목")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("결과 메시지")
                        )));
    }

    @Test
    @DisplayName("체크리스트 항목 목록 조회 API")
    void getChecklistItems() throws Exception {
        ChecklistItemResponseDTO item = ChecklistItemResponseDTO.builder()
                .id(1L)
                .checklistId(1L)
                .content("노트북 수령")
                .completed(false)
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build();

        given(checklistService.getChecklistItems(1L)).willReturn(List.of(item));

        mockMvc.perform(get("/checklists/{checklistId}/items", 1L)
                        .header(AUTH_HEADER, BEARER_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("checklists-items-list",
                        pathParameters(
                                parameterWithName("checklistId").description("체크리스트 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].id").description("아이템 ID"),
                                fieldWithPath("data[].checklistId").description("체크리스트 ID"),
                                fieldWithPath("data[].content").description("아이템 내용"),
                                fieldWithPath("data[].completed").description("완료 여부"),
                                fieldWithPath("data[].createdTime").description("생성일시"),
                                fieldWithPath("data[].updatedTime").description("수정일시")
                        )));
    }

    @Test
    @DisplayName("체크리스트 항목 추가 API")
    void addChecklistItem() throws Exception {
        ChecklistItemResponseDTO response = ChecklistItemResponseDTO.builder()
                .id(1L)
                .checklistId(1L)
                .content("보안카드 수령")
                .completed(false)
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build();

        given(checklistService.addChecklistItem(any(Long.class), any(ChecklistItemRequestDTO.class)))
                .willReturn(response);

        mockMvc.perform(post("/checklists/{checklistId}/items", 1L)
                        .header(AUTH_HEADER, BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "content": "보안카드 수령"
                                }
                                """))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("checklists-items-add",
                        pathParameters(
                                parameterWithName("checklistId").description("체크리스트 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").description("체크리스트 항목 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("아이템 ID"),
                                fieldWithPath("data.checklistId").description("체크리스트 ID"),
                                fieldWithPath("data.content").description("아이템 내용"),
                                fieldWithPath("data.completed").description("완료 여부"),
                                fieldWithPath("data.createdTime").description("생성일시"),
                                fieldWithPath("data.updatedTime").description("수정일시")
                        )));
    }

    @Test
    @DisplayName("체크리스트 항목 삭제 API")
    void deleteChecklistItem() throws Exception {
        mockMvc.perform(delete("/checklists/{checklistId}/items/{itemId}", 1L, 2L)
                        .header(AUTH_HEADER, BEARER_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("checklists-items-delete",
                        pathParameters(
                                parameterWithName("checklistId").description("체크리스트 ID"),
                                parameterWithName("itemId").description("삭제할 항목 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("결과 메시지")
                        )));
    }

    @Test
    @DisplayName("체크리스트 항목 수정 API")
    void updateChecklistItem() throws Exception {
        mockMvc.perform(put("/checklists/{checklistId}/items/{itemId}", 1L, 2L)
                        .header(AUTH_HEADER, BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "content": "사내 인트라넷 가입"
                                }
                                """))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("checklists-items-update",
                        pathParameters(
                                parameterWithName("checklistId").description("체크리스트 ID"),
                                parameterWithName("itemId").description("수정할 항목 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").description("수정할 항목 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("결과 메시지")
                        )));
    }

    @Test
    @DisplayName("체크리스트 항목 완료 상태 변경 API")
    void completeChecklistItem() throws Exception {
        mockMvc.perform(patch("/checklists/{checklistId}/items/{itemId}/complete", 1L, 2L)
                        .header(AUTH_HEADER, BEARER_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("checklists-items-complete",
                        pathParameters(
                                parameterWithName("checklistId").description("체크리스트 ID"),
                                parameterWithName("itemId").description("완료 상태 변경할 항목 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("결과 메시지")
                        )));
    }

    @Test
    @DisplayName("체크리스트 드래프트 조회 API")
    void getChecklistDraft() throws Exception {
        // given
        ChecklistDraft draft = ChecklistDraft.of("session-abc", List.of("사원증 발급", "노트북 설치"));

        given(checklistDraftService.getDraft("draft-123")).willReturn(draft);

        // when & then
        mockMvc.perform(get("/checklists/drafts/{draftId}", "draft-123")
                        .header(AUTH_HEADER, BEARER_TOKEN))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("checklists-draft-get",
                        pathParameters(
                                parameterWithName("draftId").description("드래프트 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[]").description("드래프트에 저장된 체크리스트 항목들 (문자열 리스트)")
                        )));
    }

    @Test
    @DisplayName("체크리스트 제목 + 아이템 한번에 생성 API")
    void composeChecklist() throws Exception {
        // given
        ChecklistResponseDTO response = ChecklistResponseDTO.builder()
                .id(1L)
                .userNo(100L)
                .title("오늘 업무")
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build();

        given(checklistService.composeChecklist(any(ChecklistWithItemsRequestDTO.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(post("/checklists/compose")
                        .header(AUTH_HEADER, BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "title": "오늘 업무",
                                "items": [
                                    "09:00 - 10:30: 회의1 (프로젝트 브리핑)",
                                    "11:00 - 12:00: 회의2 (팀 미팅)",
                                    "12:00 - 13:00: 점심시간",
                                    "13:00 - 14:30: 회의3 (클라이언트와의 미팅)",
                                    "14:30 - 16:00: 업무1 (보고서 작성)",
                                    "16:00 - 17:00: 업무2 (이메일 확인 및 답변)",
                                    "17:00 - 18:00: 업무3 (내일 일정 계획)"
                                ]
                            }
                            """))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("checklists-compose",
                        requestFields(
                                fieldWithPath("title").description("체크리스트 제목"),
                                fieldWithPath("items[]").description("체크리스트 항목 문자열 리스트")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("체크리스트 ID"),
                                fieldWithPath("data.userNo").description("사용자 번호"),
                                fieldWithPath("data.title").description("체크리스트 제목"),
                                fieldWithPath("data.createdTime").description("생성일시"),
                                fieldWithPath("data.updatedTime").description("수정일시")
                        )));
    }

}