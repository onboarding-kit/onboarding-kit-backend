package com.api.onboardingkit.checklist.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ChecklistDraftRequestDTO {
    private String sessionId;
    private List<String> items;
}
