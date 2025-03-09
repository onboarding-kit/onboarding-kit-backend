package com.api.onboardingkit.article.dto;

import com.api.onboardingkit.article.entity.Article;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ArticleResponseDTO {
    private Long id;
    private String category;
    private String subcategory;
    private LocalDateTime postDate; // todo. LocalDate -> LocalDateTime 변경
    private String source;
    private String title;
    private String summary;
    private int views;
    private String thumbnail;
    private String url;
    private List<String> hashtags; // ✅ 해시태그 포함

    public ArticleResponseDTO(Article article, List<String> hashtags) {
        this.id = article.getId();
        this.category = article.getCategory();
        this.subcategory = article.getSubcategory();
        this.postDate = article.getPostDate();
        this.source = article.getSource();
        this.title = article.getTitle();
        this.summary = article.getSummary();
        this.views = article.getViews();
        this.thumbnail = article.getThumbnail();
        this.url = article.getUrl();
        this.hashtags = hashtags;
    }

    // todo. Entity 변환 메서드 추가
    public static ArticleResponseDTO fromEntity(Article article, List<String> hashtags) {
        return new ArticleResponseDTO(article, hashtags);
    }
}