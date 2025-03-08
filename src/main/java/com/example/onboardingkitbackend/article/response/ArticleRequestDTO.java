package com.example.onboardingkitbackend.article.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequestDTO {
    private String category;
    private String subcategory;
    private LocalDateTime postDate;
    private String source;
    private String title;
    private String summary;
    private String thumbnail;
    private String url;
}