package com.api.onboardingkit.checklist.dto;

import com.api.onboardingkit.checklist.entity.Checklist;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChecklistResponseDTO {
    private Long id;
    private Long userNo;
    private String title;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public ChecklistResponseDTO(Checklist checklist) {
        this.id = checklist.getId();
        this.userNo = checklist.getUserNo();
        this.title = checklist.getTitle();
        this.createdTime = checklist.getCreatedTime();
        this.updatedTime = checklist.getUpdatedTime();
    }

}