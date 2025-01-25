package com.example.onboardingkitbackend.article.controller;

import com.example.onboardingkitbackend.article.model.Article;
import com.example.onboardingkitbackend.article.model.Hashtag;
import com.example.onboardingkitbackend.article.response.ArticleResponseDTO;
import com.example.onboardingkitbackend.article.service.ArticleService;
import com.example.onboardingkitbackend.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
@Import(SecurityConfig.class)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @Test
    void testFetchArticles() throws Exception {
        // 테스트 데이터 준비
        Article article = new Article();
        article.setTitle("프로젝트 셋업부터 REST API 설계까지");
        article.setCategory("개발");
        article.setSubcategory("일반");
        article.setUrl("https://f-lab.kr/insight/project-setup-to-rest-api-design");
        article.setPostDate(LocalDate.of(2024, 4, 9)); // postDate 설정

        ArticleResponseDTO article1 = new ArticleResponseDTO(article);

        List<ArticleResponseDTO> mockArticles = Arrays.asList(article1);

        // Mocking ArticleService
        Mockito.when(articleService.fetchArticles(any(), any(), any()))
                .thenReturn(mockArticles);

        // API 호출 및 검증
        mockMvc.perform(get("/api/articles")
                        .param("category", "개발")
                        .param("subcategory", "일반")
                        .param("sortBy", "latest")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    [
                        {
                            "title": "프로젝트 셋업부터 REST API 설계까지",
                            "category": "개발",
                            "subcategory": "일반",
                            "url": "https://f-lab.kr/insight/project-setup-to-rest-api-design",
                            "postDate": "2024-04-09"
                        }
                    ]
                    """));
    }
}
