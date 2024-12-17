package com.example.checklistservice.entity.dto;

public record TopCheckListsRes(
        Long id,
        String content,
        boolean icChecked
) {
}
