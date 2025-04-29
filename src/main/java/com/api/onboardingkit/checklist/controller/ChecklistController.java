package com.api.onboardingkit.checklist.controller;

import com.api.onboardingkit.checklist.dto.*;
import com.api.onboardingkit.checklist.service.ChecklistService;
import com.api.onboardingkit.config.response.dto.CustomResponse;
import com.api.onboardingkit.config.response.dto.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checklists")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;

    @GetMapping
    public ResponseEntity<CustomResponse<List<ChecklistResponseDTO>>> getUserChecklists() {
        return ResponseEntity.ok(CustomResponse.success(checklistService.getUserChecklists(), SuccessStatus.SUCCESS));
    }

    @PostMapping
    public ResponseEntity<CustomResponse<ChecklistResponseDTO>> createChecklist(
            @RequestBody ChecklistRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(CustomResponse.success(checklistService.createChecklist(requestDTO), SuccessStatus.SUCCESS));
    }

    @PutMapping("/{checklistId}/title")
    public ResponseEntity<CustomResponse<String>> updateChecklistTitle(
            @PathVariable Long checklistId,
            @RequestBody ChecklistRequestDTO requestDTO
    ) {
        checklistService.updateChecklistTitle(checklistId, requestDTO);
        return ResponseEntity.ok(CustomResponse.success("체크리스트 제목이 수정되었습니다.", SuccessStatus.SUCCESS));
    }

    @GetMapping("/{checklistId}/items")
    public ResponseEntity<CustomResponse<List<ChecklistItemResponseDTO>>> getChecklistItems(
            @PathVariable Long checklistId
    ) {
        return ResponseEntity.ok(CustomResponse.success(checklistService.getChecklistItems(checklistId), SuccessStatus.SUCCESS));
    }

    @PostMapping("/{checklistId}/items")
    public ResponseEntity<CustomResponse<ChecklistItemResponseDTO>> addChecklistItem(
            @PathVariable Long checklistId,
            @RequestBody ChecklistItemRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(CustomResponse.success(checklistService.addChecklistItem(checklistId, requestDTO), SuccessStatus.SUCCESS));
    }

    @DeleteMapping("/{checklistId}/items/{itemId}")
    public ResponseEntity<CustomResponse<String>> deleteChecklistItem(
            @PathVariable Long checklistId,
            @PathVariable Long itemId
    ) {
        checklistService.deleteChecklistItem(checklistId, itemId);
        return ResponseEntity.ok(CustomResponse.success("체크리스트 아이템이 삭제되었습니다.", SuccessStatus.SUCCESS));
    }

    @PutMapping("/{checklistId}/items/{itemId}")
    public ResponseEntity<CustomResponse<String>> updateChecklistItem(
            @PathVariable Long checklistId,
            @PathVariable Long itemId,
            @RequestBody ChecklistItemRequestDTO requestDTO
    ) {
        checklistService.updateChecklistItem(checklistId, itemId, requestDTO);
        return ResponseEntity.ok(CustomResponse.success("체크리스트 아이템이 수정되었습니다.", SuccessStatus.SUCCESS));
    }

    @PatchMapping("/{checklistId}/items/{itemId}/complete")
    public ResponseEntity<CustomResponse<String>> completeChecklistItem(
            @PathVariable Long checklistId,
            @PathVariable Long itemId
    ) {
        checklistService.completeChecklistItem(checklistId, itemId);
        return ResponseEntity.ok(CustomResponse.success("체크리스트 아이템 완료 상태가 변경되었습니다.", SuccessStatus.SUCCESS));
    }
}
