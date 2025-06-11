package com.api.onboardingkit.article.controller;

import com.api.onboardingkit.article.dto.*;
import com.api.onboardingkit.article.entity.Category;
import com.api.onboardingkit.article.service.ArticleService;
import com.api.onboardingkit.config.response.dto.CustomResponse;
import com.api.onboardingkit.config.response.dto.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "articles", description = "아티클 API")
@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @Operation(summary = "아티클 검색, 정렬, 조회")
    @GetMapping
    public CustomResponse<?> fetchArticles(@ModelAttribute ArticleSearchDTO searchDTO) {
        List<ArticleResponseDTO> articles = articleService.fetchArticles(searchDTO);
        return CustomResponse.success(articles, SuccessStatus.SUCCESS);
    }

    @Operation(summary = "아티클 추가")
    @PostMapping
    public CustomResponse<?> createArticle(@RequestBody ArticleRequestDTO requestDTO) {
        ArticleResponseDTO createdArticle = articleService.createArticle(requestDTO);
        return CustomResponse.success(createdArticle, SuccessStatus.SUCCESS);
    }

    @Operation(summary = "원본 아티클 리다이렉트")
    @GetMapping("/{id}/redirect")
    public ResponseEntity<Void> redirectToSource(@PathVariable Long id) {
        String url = articleService.incrementViewsAndGetUrl(id);
        return ResponseEntity.status(302).header("Location", url).build(); // 그대로 유지
    }

    @Operation(summary = "아티클 해시태그 추가")
    @PostMapping("/{id}/hashtags")
    public CustomResponse<?> addHashtag(
            @PathVariable Long id,
            @RequestParam(required = false) String hashtag
    ) {
        articleService.addHashtagToArticle(id, hashtag);
        return CustomResponse.success("해시태그가 성공적으로 추가되었습니다.", SuccessStatus.SUCCESS);
    }

    @Operation(summary = "카테고리 추가")
    @PostMapping("/categories")
    public CustomResponse<?> addCategories(@RequestBody CategoryRequestDTO requestDTO){
        CategoryResponseDTO createdCategories = articleService.createCategories(requestDTO);
        return CustomResponse.success(createdCategories, SuccessStatus.SUCCESS);
    }

    @Operation(summary = "존재하는 카테고리 및 서브카테고리 목록 조회")
    @GetMapping("/categories")
    public CustomResponse<?> getCategories(){
        List<CategoryResponseDTO> categories = articleService.getCategories();
        return CustomResponse.success(categories, SuccessStatus.SUCCESS);
    }

}