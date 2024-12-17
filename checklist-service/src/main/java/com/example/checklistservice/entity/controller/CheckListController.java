package com.example.checklistservice.entity.controller;

import com.example.checklistservice.entity.dto.*;
import com.example.checklistservice.entity.service.CheckListService;
import com.example.common.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/checklists")
public class CheckListController {
    private final CheckListService checkListService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createFolderWithCheckList(@Valid CreateCheckListReq createCheckListReq) {
        CreateCheckListRes folderWithCheckList = checkListService.createFolderWithCheckList(createCheckListReq);
        return ResponseEntity.ok(ApiResponse.success(folderWithCheckList));
    }

    @DeleteMapping("/{checklistId}")
    public ResponseEntity<ApiResponse<?>> deleteCheckList(@PathVariable Long checklistId) {
        checkListService.deleteCheckList(checklistId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PatchMapping("/{checklistId}")
    public ResponseEntity<ApiResponse<?>> updateCheckList(@PathVariable Long checklistId,
                                                          UpdateCheckListReq updateCheckListReqs) {
        checkListService.updateCheckLists(checklistId, updateCheckListReqs);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/{folderId}/top")
    public ResponseEntity<ApiResponse<?>> getTopCheckLists(@PathVariable Long folderId) {
        List<TopCheckListsRes> topCheckLists = checkListService.getTopCheckLists(folderId);
        return ResponseEntity.ok(ApiResponse.success(topCheckLists));
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<ApiResponse<?>> getFolderCheckLists(@PathVariable Long folderId) {
        List<CheckListsRes> folderCheckLists = checkListService.getFolderCheckLists(folderId);
        return ResponseEntity.ok(ApiResponse.success(folderCheckLists));
    }
}
