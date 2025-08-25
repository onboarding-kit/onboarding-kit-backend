package com.api.onboardingkit.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MainArticleDTO {
    private String title;
    private String summary;
    private String thumbnail;
    private String url;
    private int views;
    private String source;
    private String categoryName;
    private LocalDateTime postDate;
}