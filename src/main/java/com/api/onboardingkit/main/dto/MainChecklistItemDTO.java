package com.api.onboardingkit.main.dto;

import com.api.onboardingkit.checklist.entity.ChecklistItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MainChecklistItemDTO {
    private Long id;
    private String content;
    private LocalDateTime createdTime;

    public MainChecklistItemDTO(ChecklistItem checklistItem) {
        this.id = checklistItem.getId();
        this.content = checklistItem.getContent();
        this.createdTime = checklistItem.getCreatedTime();
    }

}