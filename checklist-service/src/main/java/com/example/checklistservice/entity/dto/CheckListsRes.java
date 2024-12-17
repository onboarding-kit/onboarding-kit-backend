package com.example.checklistservice.entity.dto;

public record CheckListsRes(
        Long checklistId,

        String content,

        boolean isChecked
) {

}
