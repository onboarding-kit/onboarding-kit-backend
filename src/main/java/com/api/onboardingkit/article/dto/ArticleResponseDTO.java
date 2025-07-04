package com.api.onboardingkit.article.dto;

import com.api.onboardingkit.article.entity.Article;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDTO {
    private Long id;
    private Long categoryId;
    private Long subcategoryId;
    private LocalDateTime postDate;
    private String source;
    private String title;
    private String summary;
    private int views;
    private String thumbnail;
    private String url;
    private List<String> hashtags;

    public ArticleResponseDTO(Article article, List<String> hashtags) {
        this.id = article.getId();
        this.categoryId = article.getCategoryId();
        this.subcategoryId = article.getSubcategoryId();
        this.postDate = article.getPostDate();
        this.source = article.getSource();
        this.title = article.getTitle();
        this.summary = article.getSummary();
        this.views = article.getViews();
        this.thumbnail = article.getThumbnail();
        this.url = article.getUrl();
        this.hashtags = hashtags;
    }

    public static ArticleResponseDTO fromEntity(Article article, List<String> hashtags) {
        return new ArticleResponseDTO(article, hashtags);
    }
}