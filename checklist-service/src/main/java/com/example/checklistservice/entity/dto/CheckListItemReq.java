package com.example.checklistservice.entity.dto;

import com.example.checklistservice.entity.CheckList;
import jakarta.validation.constraints.NotBlank;

public record CheckListItemReq(
        @NotBlank
        String content,
        boolean isChecked
) {

        public CheckList toEntity() {
                return new CheckList(null, content, isChecked(), null);
        }
}
