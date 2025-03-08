package com.api.onboardingkit.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class MainChecklistDTO {
    private Long checklistId;
    private String checklistTitle;
    private List<MainChecklistItemDTO> checklistItems;
}