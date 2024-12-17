package com.example.checklistservice.entity.dto;

import java.util.List;

public record DeleteCheckListReq(
        List<Long> checkListIds
) {
}
