package com.api.onboardingkit.article.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private String subcategory;
    private LocalDateTime postDate;
    private Long categoryId;
    private Long subcategoryId;
    private LocalDateTime postDate;
    private LocalDateTime createdTime;
    private String source;
    private String title;
    private String summary;
    private Integer views;
    private String thumbnail;
    private String url;

    public void incrementViews() {
        this.views += 1;
    }

}