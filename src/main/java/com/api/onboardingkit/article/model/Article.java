package com.api.onboardingkit.article.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// todo. Setter는 불변성을 보장하기 위해 제거하고 Builder 패턴 적용
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
    private LocalDateTime postDate; //todo. localdata 이유가 있는지 궁금, @Column(name = "post_date") 도
    private LocalDateTime createdTime;
    private String source;
    private String title;
    private String summary;
    private Integer views; // todo. 이것은 조회수인지? 의도가 궁금!
    private String thumbnail;
    private String url;

    // OneToMany remove todo. 연관성을 제거했습니다. 연관이 있을 경우 서비스 운영할 때 오류가 더 발생

    // todo. 단순 뷰 카운트라면 +1 만 해주면 되지 않을까
    public void incrementViews() {
        this.views += 1;
    }
}