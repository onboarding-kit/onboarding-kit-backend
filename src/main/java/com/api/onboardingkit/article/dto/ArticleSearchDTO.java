package com.api.onboardingkit.article.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSearchDTO {
    private Long categoryId;
    private Long subcategoryId;
    private String title;
    private String sortBy;
}