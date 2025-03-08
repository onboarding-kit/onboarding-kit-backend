package com.api.onboardingkit.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MainStatusChecklistDTO {
    private String title;
    private Integer totalItems;
    private Integer completedItems;
    private double progress;
}