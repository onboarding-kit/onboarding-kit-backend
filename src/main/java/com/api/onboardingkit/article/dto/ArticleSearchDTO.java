package com.api.onboardingkit.article.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSearchDTO {
    private String category;
    private String subcategory;
    private String title;
    private String sortBy;
}