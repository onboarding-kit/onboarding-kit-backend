package com.api.onboardingkit.prompt;

import com.api.onboardingkit.config.JwtAuthenticationFilter;
import com.api.onboardingkit.config.JwtTokenProvider;
import com.api.onboardingkit.prompt.controller.PromptController;
import com.api.onboardingkit.prompt.entity.PromptMessage;
import com.api.onboardingkit.prompt.entity.PromptSession;
import com.api.onboardingkit.prompt.service.PromptService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(PromptController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
public class PromptControllerRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PromptService promptService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("프롬프트 세션 생성 API")
    void createSession() throws Exception {
        PromptSession session = new PromptSession("session123", 1L, LocalDateTime.of(2025, 5, 12, 10, 0));
        given(promptService.createSession()).willReturn(session);

        mockMvc.perform(post("/prompt/session"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("prompt-create-session",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("세션 ID"),
                                fieldWithPath("data.createdAt").description("세션 생성 시각")
                        )));
    }

    @Test
    @DisplayName("프롬프트 세션 메시지 조회 API")
    void getMessages() throws Exception {
        PromptMessage msg = new PromptMessage(
                1L,
                "session123",
                "안녕하세요",
                true,
                LocalDateTime.of(2025, 5, 12, 10, 30)
        );

        given(promptService.getMessages("session123")).willReturn(List.of(msg));

        mockMvc.perform(get("/prompt/{sessionId}/messages", "session123"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("prompt-get-messages",
                        pathParameters(
                                parameterWithName("sessionId").description("세션 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].messageText").description("메시지 텍스트"),
                                fieldWithPath("data[].isUser").description("사용자 메시지 여부"),
                                fieldWithPath("data[].timestamp").description("메시지 전송 시각")
                        )));
    }

    @Test
    @DisplayName("프롬프트 메시지 전송 API")
    void sendMessage() throws Exception {
        PromptMessage response = new PromptMessage(
                2L,
                "session123",
                "안녕하세요, 무엇을 도와드릴까요?",
                false,
                LocalDateTime.of(2025, 5, 12, 10, 31)
        );

        given(promptService.sendMessage("session123", "안녕하세요")).willReturn(response);

        mockMvc.perform(post("/prompt/{sessionId}/messages", "session123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "message": "안녕하세요"
                                }
                                """))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("prompt-send-message",
                        pathParameters(
                                parameterWithName("sessionId").description("세션 ID")
                        ),
                        requestFields(
                                fieldWithPath("message").description("보낼 메시지 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.messageText").description("메시지 내용"),
                                fieldWithPath("data.isUser").description("사용자인지 여부"),
                                fieldWithPath("data.timestamp").description("전송 시각")
                        )));
    }
}
