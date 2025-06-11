package com.api.onboardingkit.checklist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistWithItemsRequestDTO {
    private String title;
    private List<String> items;
}
