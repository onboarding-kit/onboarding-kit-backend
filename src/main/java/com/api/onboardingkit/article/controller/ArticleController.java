package com.api.onboardingkit.article.controller;

import com.api.onboardingkit.article.response.ArticleRequestDTO;
import com.api.onboardingkit.article.response.ArticleResponseDTO;
import com.api.onboardingkit.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleResponseDTO>> fetchArticles(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String subcategory,
            @RequestParam(defaultValue = "latest") String sortBy) {
        // 조건이 없는 경우 모든 아티클을 반환
        List<ArticleResponseDTO> articles = articleService.fetchArticles(category, subcategory, sortBy);
        return ResponseEntity.ok(articles);
    }

    @PostMapping
    public ResponseEntity<ArticleResponseDTO> createArticle(@RequestBody ArticleRequestDTO requestDTO) {
        ArticleResponseDTO createdArticle = articleService.createArticle(requestDTO);
        return ResponseEntity.ok(createdArticle);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArticleResponseDTO>> searchArticles(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String subcategory,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "latest") String sortBy) {
        List<ArticleResponseDTO> articles = articleService.searchArticles(category, subcategory, searchTerm, sortBy);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}/redirect")
    public ResponseEntity<Void> redirectToSource(@PathVariable Long id) {
        String url = articleService.incrementViewsAndGetUrl(id);
        return ResponseEntity.status(302).header("Location", url).build();
    }

    @PostMapping("/{id}/hashtags")
    public ResponseEntity<String> addHashtags(
            @PathVariable Long id,
            @RequestBody List<String> hashtags) {
        articleService.addHashtagsToArticle(id, hashtags);
        return ResponseEntity.ok("해시태그가 성공적으로 추가되었습니다.");
    }
}