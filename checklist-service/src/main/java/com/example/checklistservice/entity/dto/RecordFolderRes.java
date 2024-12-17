package com.example.checklistservice.entity.dto;

import java.time.LocalDateTime;

public record RecordFolderRes(
        Long folderId,

        String title,

        long checkListCount,

        LocalDateTime lastUpdatedTime
) {
}
