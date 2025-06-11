package com.api.onboardingkit.article;

import com.api.onboardingkit.article.controller.ArticleController;
import com.api.onboardingkit.article.dto.*;
import com.api.onboardingkit.article.service.ArticleService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.restdocs.request.RequestDocumentation.*;

@WebMvcTest(ArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
public class ArticleControllerRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("아티클 목록 조회 API - 성공")
    void fetchArticles() throws Exception {
        ArticleResponseDTO response = ArticleResponseDTO.builder()
                .id(1L)
                .categoryId(1L)
                .subcategoryId(2L)
                .postDate(LocalDateTime.parse("2024-03-01T10:00:00"))
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
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].id").description("아티클 ID"),
                                fieldWithPath("data[].categoryId").description("카테고리 ID"),
                                fieldWithPath("data[].subcategoryId").description("서브카테고리 ID"),
                                fieldWithPath("data[].postDate").description("게시일"),
                                fieldWithPath("data[].source").description("출처"),
                                fieldWithPath("data[].title").description("제목"),
                                fieldWithPath("data[].summary").description("요약"),
                                fieldWithPath("data[].views").description("조회수"),
                                fieldWithPath("data[].thumbnail").description("썸네일 이미지 경로"),
                                fieldWithPath("data[].url").description("기사 링크"),
                                fieldWithPath("data[].hashtags").description("해시태그 리스트")
                        )));
    }

    @Test
    @DisplayName("아티클 생성 API - 성공")
    void createArticle() throws Exception {
        ArticleRequestDTO request = ArticleRequestDTO.builder()
                .categoryId(1L)
                .subcategoryId(2L)
                .postDate(LocalDateTime.now())
                .source("GZ")
                .title("제목")
                .summary("요약")
                .thumbnail("thumb.jpg")
                .url("https://example.com")
                .build();

        ArticleResponseDTO response = ArticleResponseDTO.builder()
                .id(1L)
                .categoryId(request.getCategoryId())
                .subcategoryId(request.getSubcategoryId())
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
                                    "categoryId": 1,
                                    "subcategoryId": 2,
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
                                fieldWithPath("categoryId").description("카테고리 ID"),
                                fieldWithPath("subcategoryId").description("서브카테고리 ID"),
                                fieldWithPath("postDate").description("게시일"),
                                fieldWithPath("source").description("출처"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("summary").description("요약"),
                                fieldWithPath("thumbnail").description("썸네일"),
                                fieldWithPath("url").description("기사 링크")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("아티클 ID"),
                                fieldWithPath("data.categoryId").description("카테고리 ID"),
                                fieldWithPath("data.subcategoryId").description("서브카테고리 ID"),
                                fieldWithPath("data.postDate").description("게시일"),
                                fieldWithPath("data.source").description("출처"),
                                fieldWithPath("data.title").description("제목"),
                                fieldWithPath("data.summary").description("요약"),
                                fieldWithPath("data.views").description("조회수"),
                                fieldWithPath("data.thumbnail").description("썸네일"),
                                fieldWithPath("data.url").description("기사 링크"),
                                fieldWithPath("data.hashtags").description("해시태그 리스트")
                        )));
    }

    @Test
    @DisplayName("아티클 해시태그 추가 API - 성공")
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
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("결과 메시지")
                        )));
    }

    @Test
    @DisplayName("아티클 리다이렉트 API - 성공")
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

    @Test
    @DisplayName("카테고리 추가 API - 성공")
    void addCategory() throws Exception {
        CategoryRequestDTO request = CategoryRequestDTO.builder()
                .categoryName("기획")
                .depth(0)
                .parentId(1L)
                .build();

        CategoryResponseDTO response = CategoryResponseDTO.builder()
                .id(1L)
                .categoryName("기획")
                .depth(0)
                .parentId(1L)
                .createdAt(LocalDateTime.parse("2024-03-01T10:00:00"))
                .build();

        given(articleService.createCategories(any(CategoryRequestDTO.class)))
                .willReturn(response);

        mockMvc.perform(post("/articles/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "categoryName": "기획",
                                "depth": 0,
                                "parentId": null
                            }
                            """))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("articles-category-create",
                        requestFields(
                                fieldWithPath("categoryName").description("카테고리 이름"),
                                fieldWithPath("depth").description("카테고리(개발/기획/다자인)는 0, 서브카테고리는 1"),
                                fieldWithPath("parentId").description("부모 카테고리 ID (서브카테고리인 경우)").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("카테고리 ID"),
                                fieldWithPath("data.categoryName").description("카테고리 이름"),
                                fieldWithPath("data.depth").description("카테고리(개발/기획/다자인)는 0, 서브카테고리는 1"),
                                fieldWithPath("data.parentId").description("부모 카테고리 ID"),
                                fieldWithPath("data.createdAt").description("생성일시")
                        )));
    }

    @Test
    @DisplayName("카테고리 목록 조회 API - 성공")
    void getCategories() throws Exception {
        CategoryResponseDTO category1 = CategoryResponseDTO.builder()
                .id(1L)
                .categoryName("기획")
                .depth(0)
                .parentId(1L)
                .createdAt(LocalDateTime.parse("2024-03-01T10:00:00"))
                .build();

        CategoryResponseDTO category2 = CategoryResponseDTO.builder()
                .id(2L)
                .categoryName("UX")
                .depth(1)
                .parentId(1L)
                .createdAt(LocalDateTime.parse("2024-03-02T10:00:00"))
                .build();

        given(articleService.getCategories())
                .willReturn(List.of(category1, category2));

        mockMvc.perform(get("/articles/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("articles-category-list",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].id").description("카테고리 ID"),
                                fieldWithPath("data[].categoryName").description("카테고리 이름"),
                                fieldWithPath("data[].depth").description("카테고리(개발/기획/다자인)는 0, 서브카테고리는 1"),
                                fieldWithPath("data[].parentId").description("부모 카테고리 ID"),
                                fieldWithPath("data[].createdAt").description("생성일시")
                        )));
    }

}
