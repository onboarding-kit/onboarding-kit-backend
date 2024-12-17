package com.example.checklistservice.entity.dto;

public record UpdateCheckListReq(

        String content,

        Boolean isChecked
) {
}
