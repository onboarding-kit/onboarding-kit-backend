package com.api.onboardingkit.checklist.controller;

import com.api.onboardingkit.checklist.draft.ChecklistDraft;
import com.api.onboardingkit.checklist.dto.*;
import com.api.onboardingkit.checklist.service.ChecklistDraftService;
import com.api.onboardingkit.checklist.service.ChecklistService;
import com.api.onboardingkit.config.response.dto.CustomResponse;
import com.api.onboardingkit.config.response.dto.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checklists")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;
    private final ChecklistDraftService checklistDraftService;

    @GetMapping
    public CustomResponse<?> getUserChecklists() {
        return CustomResponse.success(checklistService.getUserChecklists(), SuccessStatus.SUCCESS);
    }

    @PostMapping
    public CustomResponse<?> createChecklist(@RequestBody ChecklistRequestDTO requestDTO) {
        return CustomResponse.success(checklistService.createChecklist(requestDTO), SuccessStatus.SUCCESS);
    }

    @PutMapping("/{checklistId}/title")
    public CustomResponse<?> updateChecklistTitle(
            @PathVariable Long checklistId,
            @RequestBody ChecklistRequestDTO requestDTO
    ) {
        checklistService.updateChecklistTitle(checklistId, requestDTO);
        return CustomResponse.success("체크리스트 제목이 수정되었습니다.", SuccessStatus.SUCCESS);
    }

    @GetMapping("/{checklistId}/items")
    public CustomResponse<?> getChecklistItems(@PathVariable Long checklistId) {
        return CustomResponse.success(checklistService.getChecklistItems(checklistId), SuccessStatus.SUCCESS);
    }

    @PostMapping("/{checklistId}/items")
    public CustomResponse<?> addChecklistItem(
            @PathVariable Long checklistId,
            @RequestBody ChecklistItemRequestDTO requestDTO
    ) {
        return CustomResponse.success(checklistService.addChecklistItem(checklistId, requestDTO), SuccessStatus.SUCCESS);
    }

    @DeleteMapping("/{checklistId}/items/{itemId}")
    public CustomResponse<?> deleteChecklistItem(
            @PathVariable Long checklistId,
            @PathVariable Long itemId
    ) {
        checklistService.deleteChecklistItem(checklistId, itemId);
        return CustomResponse.success("체크리스트 아이템이 삭제되었습니다.", SuccessStatus.SUCCESS);
    }

    @PutMapping("/{checklistId}/items/{itemId}")
    public CustomResponse<?> updateChecklistItem(
            @PathVariable Long checklistId,
            @PathVariable Long itemId,
            @RequestBody ChecklistItemRequestDTO requestDTO
    ) {
        checklistService.updateChecklistItem(checklistId, itemId, requestDTO);
        return CustomResponse.success("체크리스트 아이템이 수정되었습니다.", SuccessStatus.SUCCESS);
    }

    @PatchMapping("/{checklistId}/items/{itemId}/complete")
    public CustomResponse<?> completeChecklistItem(
            @PathVariable Long checklistId,
            @PathVariable Long itemId
    ) {
        checklistService.completeChecklistItem(checklistId, itemId);
        return CustomResponse.success("체크리스트 아이템 완료 상태가 변경되었습니다.", SuccessStatus.SUCCESS);
    }

    @GetMapping("/drafts/{draftId}")
    public CustomResponse<?> getDraft(@PathVariable String draftId) {
        ChecklistDraft draft = checklistDraftService.getDraft(draftId);
        return CustomResponse.success(draft.getItems(), SuccessStatus.SUCCESS);
    }

}
