package com.example.checklistservice.entity.dto;

import java.util.List;

public record CreateCheckListRes(
        Long folderId,
        List<Long> checkListIds
) {
}
