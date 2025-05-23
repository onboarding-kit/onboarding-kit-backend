package com.api.onboardingkit.article.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSearchDTO {
    private String category;
    private String subcategory;
    private String title;
    private String sortBy;
}