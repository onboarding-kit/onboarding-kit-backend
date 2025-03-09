package com.api.onboardingkit.checklist.controller;

import com.api.onboardingkit.checklist.dto.*;
import com.api.onboardingkit.checklist.service.ChecklistService;
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
    public ResponseEntity<List<ChecklistResponseDTO>> getUserChecklists(
    ) {
        return ResponseEntity.ok(checklistService.getUserChecklists());
    }

    @PostMapping
    public ResponseEntity<ChecklistResponseDTO> createChecklist(
            @RequestBody ChecklistRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(checklistService.createChecklist(requestDTO));
    }

    @PutMapping("/{checklistId}/title")
    public ResponseEntity<String> updateChecklistTitle(
            @PathVariable Long checklistId,
            @RequestBody ChecklistRequestDTO requestDTO
    ) {
        checklistService.updateChecklistTitle(checklistId, requestDTO);
        return ResponseEntity.ok("체크리스트 제목이 수정되었습니다.");
    }

    @GetMapping("/{checklistId}/items")
    public ResponseEntity<List<ChecklistItemResponseDTO>> getChecklistItems(
            @PathVariable Long checklistId
    ) {
        return ResponseEntity.ok(checklistService.getChecklistItems(checklistId));
    }

    @PostMapping("/{checklistId}/items")
    public ResponseEntity<ChecklistItemResponseDTO> addChecklistItem(
            @PathVariable Long checklistId,
            @RequestBody ChecklistItemRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(checklistService.addChecklistItem(checklistId, requestDTO));
    }

    @DeleteMapping("/{checklistId}/items/{itemId}")
    public ResponseEntity<String> deleteChecklistItem(
            @PathVariable Long checklistId,
            @PathVariable Long itemId
    ) {
        checklistService.deleteChecklistItem(checklistId, itemId);
        return ResponseEntity.ok("체크리스트 아이템이 삭제되었습니다.");
    }

    @PutMapping("/{checklistId}/items/{itemId}")
    public ResponseEntity<String> updateChecklistItem(
            @PathVariable Long checklistId,
            @PathVariable Long itemId,
            @RequestBody ChecklistItemRequestDTO requestDTO
    ) {
        checklistService.updateChecklistItem(checklistId, itemId, requestDTO);
        return ResponseEntity.ok("체크리스트 아이템이 수정되었습니다.");
    }

    @PatchMapping("/{checklistId}/items/{itemId}/complete")
    public ResponseEntity<String> completeChecklistItem(
            @PathVariable Long checklistId,
            @PathVariable Long itemId
    ) {
        checklistService.completeChecklistItem(checklistId, itemId);
        return ResponseEntity.ok("체크리스트 아이템 완료 상태가 변경되었습니다.");
    }

}