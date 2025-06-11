package com.api.onboardingkit.article.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequestDTO {
    private Long categoryId;
    private Long subcategoryId;
    private LocalDateTime postDate;
    private String source;
    private String title;
    private String summary;
    private String thumbnail;
    private String url;

}