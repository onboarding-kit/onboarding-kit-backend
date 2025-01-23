package com.example.onboardingkitbackend.article.controller;

import com.example.onboardingkitbackend.article.model.Article;
import com.example.onboardingkitbackend.article.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ArticleRepository articleRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    void setUp() {
        // 데이터베이스 초기화
        articleRepository.deleteAll();

        // 테스트 데이터 삽입
        Article article = new Article();
        article.setCategory("개발");
        article.setSubcategory("일반");
        article.setPostDate(LocalDate.of(2024, 4, 9)); // LocalDate 설정
        article.setCreatedTime(LocalDateTime.now());
        article.setSource("https://f-lab.kr");
        article.setTitle("프로젝트 셋업부터 REST API 설계까지");
        article.setSummary("REST API 설계 및 프로젝트 셋업 가이드");
        article.setViews(0);
        article.setThumbnail(null);
        article.setUrl("https://f-lab.kr/insight/project-setup-to-rest-api-design");

        articleRepository.save(article);
    }

    @Test
    void testFetchArticles() {
        // 요청 URL
        String url = "http://localhost:" + port + "/api/articles";

        // API 호출
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // 검증
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("프로젝트 셋업부터 REST API 설계까지");
        assertThat(response.getBody()).contains("개발");
        assertThat(response.getBody()).contains("일반");
        assertThat(response.getBody()).contains("2024-04-09"); // postDate 확인
    }
}
