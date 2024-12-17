package com.example.checklistservice.entity.dto;

public record HomeFolderRes(
        Long folderId,

        String title,

        int completionRate
) {
}
