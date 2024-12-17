package com.example.checklistservice.entity.dto;

import com.example.checklistservice.entity.Folder;
import jakarta.validation.constraints.NotBlank;

public record FolderInfoReq(
        @NotBlank
        String title
) {

        public Folder toEntity() {
                return new Folder(null, title, null);
        }
}
