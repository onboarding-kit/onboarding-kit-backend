package com.api.onboardingkit.article;

import com.api.onboardingkit.article.controller.ArticleController;
import com.api.onboardingkit.article.dto.ArticleRequestDTO;
import com.api.onboardingkit.article.dto.ArticleResponseDTO;
import com.api.onboardingkit.article.dto.ArticleSearchDTO;
import com.api.onboardingkit.article.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.restdocs.request.RequestDocumentation.*;

@WebMvcTest(ArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
public class ArticleControllerRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArticleService articleService;

    @Test
    @DisplayName("아티클 목록 조회 API")
    void fetchArticles() throws Exception {
        ArticleResponseDTO response = ArticleResponseDTO.builder()
                .id(1L)
                .category("가이드")
                .subcategory("입사")
                .postDate(LocalDateTime.now())
                .source("GZ")
                .title("제목")
                .summary("요약")
                .views(123)
                .thumbnail("thumb.jpg")
                .url("https://example.com")
                .hashtags(List.of("가이드"))
                .build();

        given(articleService.fetchArticles(any(ArticleSearchDTO.class)))
                .willReturn(List.of(response));

        mockMvc.perform(get("/articles")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("articles-list",
                        responseFields(
                                fieldWithPath("[].id").description("아티클 ID"),
                                fieldWithPath("[].category").description("카테고리"),
                                fieldWithPath("[].subcategory").description("서브카테고리"),
                                fieldWithPath("[].postDate").description("게시일"),
                                fieldWithPath("[].source").description("출처"),
                                fieldWithPath("[].title").description("제목"),
                                fieldWithPath("[].summary").description("요약"),
                                fieldWithPath("[].views").description("조회수"),
                                fieldWithPath("[].thumbnail").description("썸네일 이미지 경로"),
                                fieldWithPath("[].url").description("기사 링크"),
                                fieldWithPath("[].hashtags").description("해시태그 리스트")
                        )));
    }

    @Test
    @DisplayName("아티클 생성 API")
    void createArticle() throws Exception {
        ArticleRequestDTO request = ArticleRequestDTO.builder()
                .category("가이드")
                .subcategory("입사")
                .postDate(LocalDateTime.now())
                .source("GZ")
                .title("제목")
                .summary("요약")
                .thumbnail("thumb.jpg")
                .url("https://example.com")
                .build();

        ArticleResponseDTO response = ArticleResponseDTO.builder()
                .id(1L)
                .category(request.getCategory())
                .subcategory(request.getSubcategory())
                .postDate(request.getPostDate())
                .source(request.getSource())
                .title(request.getTitle())
                .summary(request.getSummary())
                .views(0)
                .thumbnail(request.getThumbnail())
                .url(request.getUrl())
                .hashtags(List.of())
                .build();

        given(articleService.createArticle(any(ArticleRequestDTO.class)))
                .willReturn(response);

        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "category": "가이드",
                                    "subcategory": "입사",
                                    "postDate": "2024-03-01T10:00:00",
                                    "source": "GZ",
                                    "title": "제목",
                                    "summary": "요약",
                                    "thumbnail": "thumb.jpg",
                                    "url": "https://example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("articles-create",
                        requestFields(
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("subcategory").description("서브카테고리"),
                                fieldWithPath("postDate").description("게시일"),
                                fieldWithPath("source").description("출처"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("summary").description("요약"),
                                fieldWithPath("thumbnail").description("썸네일"),
                                fieldWithPath("url").description("기사 링크")
                        ),
                        responseFields(
                                fieldWithPath("id").description("아티클 ID"),
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("subcategory").description("서브카테고리"),
                                fieldWithPath("postDate").description("게시일"),
                                fieldWithPath("source").description("출처"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("summary").description("요약"),
                                fieldWithPath("views").description("조회수"),
                                fieldWithPath("thumbnail").description("썸네일"),
                                fieldWithPath("url").description("기사 링크"),
                                fieldWithPath("hashtags").description("해시태그 리스트")
                        )));
    }

    @Test
    @DisplayName("아티클 해시태그 추가 API")
    void addHashtag() throws Exception {
        mockMvc.perform(post("/articles/{id}/hashtags", 1L)
                        .queryParam("hashtag", "온보딩"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("articles-add-hashtag",
                        pathParameters(
                                parameterWithName("id").description("아티클 ID")
                        ),
                        queryParameters(
                                parameterWithName("hashtag").description("추가할 해시태그")
                        )));
    }

    @Test
    @DisplayName("아티클 리다이렉트 API")
    void redirectToSource() throws Exception {
        given(articleService.incrementViewsAndGetUrl(1L))
                .willReturn("https://example.com");

        mockMvc.perform(get("/articles/{id}/redirect", 1L))
                .andExpect(status().isFound())
                .andDo(print())
                .andDo(document("articles-redirect",
                        pathParameters(
                                parameterWithName("id").description("아티클 ID")
                        )));
    }
}
