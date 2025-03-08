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
    public ResponseEntity<List<ArticleResponseDTO>> fetchArticles(ArticleSearchDTO searchDTO) { // todo. searchdto 추가
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

    // todo. search 제거. 검색용 API를 따로 뺀 이유가 있는지 ?.? 아티클 조회로 통합

    @GetMapping("/{id}/redirect") // todo. 이친구는 의도가 궁금 단순 페이지 뷰 count +1 인지!
    public ResponseEntity<Void> redirectToSource(
            @PathVariable Long id
    ) {
        String url = articleService.incrementViewsAndGetUrl(id);
        return ResponseEntity.status(302).header("Location", url).build();
    }

    @PostMapping("/{id}/hashtags")
    public ResponseEntity<String> addHashtag( // todo. 하나의 해시태그를 저장
            @PathVariable Long id,
            @RequestParam(required = false) String hashtag
    ) {
        articleService.addHashtagToArticle(id, hashtag);
        return ResponseEntity.ok("해시태그가 성공적으로 추가되었습니다.");
    }

}