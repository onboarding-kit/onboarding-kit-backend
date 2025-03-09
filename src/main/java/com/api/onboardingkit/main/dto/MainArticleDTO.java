package com.api.onboardingkit.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MainArticleDTO {
    private String title;
    private String summary;
    private String thumbnail;
    private String url;
    private int views;
}