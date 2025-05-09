package com.api.onboardingkit.checklist.service;

import com.api.onboardingkit.checklist.dto.ChecklistItemRequestDTO;
import com.api.onboardingkit.checklist.dto.ChecklistItemResponseDTO;
import com.api.onboardingkit.checklist.dto.ChecklistRequestDTO;
import com.api.onboardingkit.checklist.dto.ChecklistResponseDTO;
import com.api.onboardingkit.checklist.entity.Checklist;
import com.api.onboardingkit.checklist.entity.ChecklistItem;
import com.api.onboardingkit.checklist.repository.ChecklistRepository;
import com.api.onboardingkit.checklist.repository.ChecklistItemRepository;
import com.api.onboardingkit.config.AbstractService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChecklistService extends AbstractService {

    private final ChecklistRepository checklistRepository;
    private final ChecklistItemRepository checklistItemRepository;

    public List<ChecklistResponseDTO> getUserChecklists() {
        return checklistRepository.findByUserNo(getMemberId()).stream()
                .map(ChecklistResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChecklistResponseDTO createChecklist(ChecklistRequestDTO requestDTO) {
        Checklist checklist = Checklist.builder()
                .userNo(getMemberId())
                .title(requestDTO.getTitle())
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build();

        return new ChecklistResponseDTO(checklistRepository.save(checklist));
    }

    @Transactional
    public void updateChecklistTitle(Long checklistId, ChecklistRequestDTO requestDTO) {
        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> new IllegalArgumentException("체크리스트를 찾을 수 없습니다."));

        checklist.updateTitle(requestDTO.getTitle());
    }

    public List<ChecklistItemResponseDTO> getChecklistItems(Long checklistId) {
        return checklistItemRepository.findByChecklistId(checklistId).stream()
                .map(ChecklistItemResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChecklistItemResponseDTO addChecklistItem(Long checklistId, ChecklistItemRequestDTO requestDTO) {
        ChecklistItem checklistItem = ChecklistItem.builder()
                .checklistId(checklistId)
                .content(requestDTO.getContent())
                .completed(false)
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build();

        return new ChecklistItemResponseDTO(checklistItemRepository.save(checklistItem));
    }

    @Transactional
    public void deleteChecklistItem(Long checklistId, Long itemId) {
        ChecklistItem checklistItem = checklistItemRepository.findByChecklistIdAndId(checklistId, itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 체크리스트 아이템을 찾을 수 없습니다."));

        checklistItemRepository.delete(checklistItem);
    }

    @Transactional
    public void updateChecklistItem(Long checklistId, Long itemId, ChecklistItemRequestDTO requestDTO) {
        ChecklistItem checklistItem = checklistItemRepository.findByChecklistIdAndId(checklistId, itemId)
                .orElseThrow(() -> new IllegalArgumentException("체크리스트 아이템을 찾을 수 없습니다."));

        checklistItem.updateContent(requestDTO.getContent());
    }

    @Transactional
    public void completeChecklistItem(Long checklistId, Long itemId) {
        ChecklistItem checklistItem = checklistItemRepository.findByChecklistIdAndId(checklistId, itemId)
                .orElseThrow(() -> new IllegalArgumentException("체크리스트 아이템을 찾을 수 없습니다."));

        checklistItem.toggleCompleted();
    }

}