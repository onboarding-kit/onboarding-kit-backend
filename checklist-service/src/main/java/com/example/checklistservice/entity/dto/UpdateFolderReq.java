package com.example.checklistservice.entity.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateFolderReq(
        @NotBlank
        String title
) {
}
