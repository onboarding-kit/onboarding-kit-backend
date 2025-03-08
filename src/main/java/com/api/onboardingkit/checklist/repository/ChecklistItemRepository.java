package com.api.onboardingkit.checklist.repository;

import com.api.onboardingkit.checklist.entity.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {
    List<ChecklistItem> findByChecklistId(Long checklistId);
    Optional<ChecklistItem> findByChecklistIdAndId(Long checklistId, Long itemId);
    Integer countByChecklistId(Long checklistId);
    Integer countByChecklistIdAndCompleted(Long checklistId, boolean completed);
    List<ChecklistItem> findTop3ByChecklistIdAndCompletedFalseOrderByCreatedTimeDesc(Long checklistId);
}