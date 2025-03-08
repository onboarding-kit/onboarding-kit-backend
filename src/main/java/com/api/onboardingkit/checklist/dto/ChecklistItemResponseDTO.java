package com.api.onboardingkit.checklist.dto;

import com.api.onboardingkit.checklist.entity.ChecklistItem;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChecklistItemResponseDTO {
    private Long id;
    private Long checklistId;
    private String content;
    private Boolean completed;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public ChecklistItemResponseDTO(ChecklistItem checklistItem) {
        this.id = checklistItem.getId();
        this.checklistId = checklistItem.getChecklistId();
        this.content = checklistItem.getContent();
        this.completed = checklistItem.getCompleted();
        this.createdTime = checklistItem.getCreatedTime();
        this.updatedTime = checklistItem.getUpdatedTime();
    }
}