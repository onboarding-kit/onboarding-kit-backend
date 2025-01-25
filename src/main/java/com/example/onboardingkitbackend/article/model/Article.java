package com.example.onboardingkitbackend.article.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private String subcategory;
    @Column(name = "post_date")
    private LocalDate postDate;
    private LocalDateTime createdTime;
    private String source;
    private String title;
    private String summary;
    private int views;
    private String thumbnail;
    private String url;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hashtag> hashtags = new ArrayList<>();
}