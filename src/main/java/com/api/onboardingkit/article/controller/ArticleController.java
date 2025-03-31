package com.api.onboardingkit.article.controller;

import com.api.onboardingkit.article.dto.ArticleRequestDTO;
import com.api.onboardingkit.article.dto.ArticleResponseDTO;
import com.api.onboardingkit.article.dto.ArticleSearchDTO;
import com.api.onboardingkit.article.service.ArticleService;
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
    public ResponseEntity<List<ArticleResponseDTO>> fetchArticles(ArticleSearchDTO searchDTO) {
        List<ArticleResponseDTO> articles = articleService.fetchArticles(searchDTO);
        return ResponseEntity.ok(articles);
    }

    @PostMapping
    public ResponseEntity<ArticleResponseDTO> createArticle(
            @RequestBody ArticleRequestDTO requestDTO
    ) {
        ArticleResponseDTO createdArticle = articleService.createArticle(requestDTO);
        return ResponseEntity.ok(createdArticle);
    }

    @GetMapping("/{id}/redirect")
    public ResponseEntity<Void> redirectToSource(
            @PathVariable Long id
    ) {
        String url = articleService.incrementViewsAndGetUrl(id);
        return ResponseEntity.status(302).header("Location", url).build();
    }

    @PostMapping("/{id}/hashtags")
    public ResponseEntity<String> addHashtag(
            @PathVariable Long id,
            @RequestParam(required = false) String hashtag
    ) {
        articleService.addHashtagToArticle(id, hashtag);
        return ResponseEntity.ok("해시태그가 성공적으로 추가되었습니다.");
    }

}