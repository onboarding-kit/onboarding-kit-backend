package com.api.onboardingkit.checklist.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Checklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userNo;
    private String title;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public void updateTitle(String title) {
        this.title = title;
        this.updatedTime = LocalDateTime.now();
    }
}