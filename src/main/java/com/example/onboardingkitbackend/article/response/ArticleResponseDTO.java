package com.example.onboardingkitbackend.article.response;

import com.example.onboardingkitbackend.article.model.Article;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleResponseDTO {
    private Long id;
    private String category;
    private String subcategory;
    private String postDate;
    private String source;
    private String title;
    private String summary;
    private int views;
    private String thumbnail;
    private String url;

    public ArticleResponseDTO(Article article) {
        this.id = article.getId();
        this.category = article.getCategory();
        this.subcategory = article.getSubcategory();
        this.postDate = article.getPostDate().toString();
        this.source = article.getSource();
        this.title = article.getTitle();
        this.summary = article.getSummary();
        this.views = article.getViews();
        this.thumbnail = article.getThumbnail();
        this.url = article.getUrl();
    }
}