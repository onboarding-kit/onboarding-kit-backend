package com.example.checklistservice.entity.service;

import com.example.checklistservice.entity.dto.*;

import java.util.List;

public interface CheckListService {
    CreateCheckListRes createFolderWithCheckList(CreateCheckListReq createCheckListReq);

    void deleteCheckList(Long checklistId);

    void updateCheckLists(Long checklistId, UpdateCheckListReq updateCheckListReqs);

    List<TopCheckListsRes> getTopCheckLists(Long folderId);

    List<CheckListsRes> getFolderCheckLists(Long folderId);
}
