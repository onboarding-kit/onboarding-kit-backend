package com.api.onboardingkit.checklist.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long checklistId;
    private String content;
    private Boolean completed;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public void updateContent(String content) {
        this.content = content;
        this.updatedTime = LocalDateTime.now();
    }

    public void toggleCompleted() {
        this.completed = !this.completed;
        this.updatedTime = LocalDateTime.now();
    }
}