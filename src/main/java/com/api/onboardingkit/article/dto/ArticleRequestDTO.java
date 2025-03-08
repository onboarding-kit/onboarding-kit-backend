package com.api.onboardingkit.article.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
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