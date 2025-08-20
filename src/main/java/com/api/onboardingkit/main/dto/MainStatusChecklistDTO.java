package com.api.onboardingkit.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MainStatusChecklistDTO {
    private Long checklistId;
    private String title;
    private Integer totalItems;
    private Integer completedItems;
    private double progress;
}