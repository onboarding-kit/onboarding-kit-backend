package com.example.checklistservice.entity.dto;

import java.util.List;

public record CreateCheckListReq(
        FolderInfoReq folderInfoReq,

        List<CheckListItemReq> checkListItemReqList
) {
}
