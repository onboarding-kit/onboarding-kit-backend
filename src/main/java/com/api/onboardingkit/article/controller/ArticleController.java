package com.api.onboardingkit.article.controller;

import com.api.onboardingkit.article.dto.ArticleRequestDTO;
import com.api.onboardingkit.article.dto.ArticleResponseDTO;
import com.api.onboardingkit.article.dto.ArticleSearchDTO;
import com.api.onboardingkit.article.service.ArticleService;
import com.api.onboardingkit.config.response.dto.CustomResponse;
import com.api.onboardingkit.config.response.dto.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    public CustomResponse<?> fetchArticles(ArticleSearchDTO searchDTO) {
        List<ArticleResponseDTO> articles = articleService.fetchArticles(searchDTO);
        return CustomResponse.success(articles, SuccessStatus.SUCCESS);
    }

    @PostMapping
    public CustomResponse<?> createArticle(@RequestBody ArticleRequestDTO requestDTO) {
        ArticleResponseDTO createdArticle = articleService.createArticle(requestDTO);
        return CustomResponse.success(createdArticle, SuccessStatus.SUCCESS);
    }

    @GetMapping("/{id}/redirect")
    public ResponseEntity<Void> redirectToSource(@PathVariable Long id) {
        String url = articleService.incrementViewsAndGetUrl(id);
        return ResponseEntity.status(302).header("Location", url).build(); // 그대로 유지
    }

    @PostMapping("/{id}/hashtags")
    public CustomResponse<?> addHashtag(
            @PathVariable Long id,
            @RequestParam(required = false) String hashtag
    ) {
        articleService.addHashtagToArticle(id, hashtag);
        return CustomResponse.success("해시태그가 성공적으로 추가되었습니다.", SuccessStatus.SUCCESS);
    }
}