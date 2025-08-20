package com.api.onboardingkit.main.service;

import com.api.onboardingkit.article.repository.ArticleRepository;
import com.api.onboardingkit.checklist.entity.Checklist;
import com.api.onboardingkit.checklist.repository.ChecklistItemRepository;
import com.api.onboardingkit.checklist.repository.ChecklistRepository;
import com.api.onboardingkit.config.AbstractService;
import com.api.onboardingkit.config.exception.CustomException;
import com.api.onboardingkit.config.exception.ErrorCode;
import com.api.onboardingkit.main.dto.MainArticleDTO;
import com.api.onboardingkit.main.dto.MainChecklistItemDTO;
import com.api.onboardingkit.main.dto.MainStatusChecklistDTO;
import com.api.onboardingkit.main.dto.MainChecklistDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MainService extends AbstractService {

    private final ArticleRepository articleRepository;
    private final ChecklistRepository checklistRepository;
    private final ChecklistItemRepository checklistItemRepository;

    public List<MainStatusChecklistDTO> getMainChecklistStatus() {
        return checklistRepository.findByUserNo(getMemberId()).stream()
                .map(checklist -> {
                    Integer totalItems = checklistItemRepository.countByChecklistId(checklist.getId());
                    Integer completedItems = checklistItemRepository.countByChecklistIdAndCompleted(checklist.getId(), true);
                    double progress = totalItems > 0 ? (completedItems * 100.0 / totalItems) : 0.0;

                    return MainStatusChecklistDTO.builder()
                            .checklistId(checklist.getId())
                            .title(checklist.getTitle())
                            .totalItems(totalItems)
                            .completedItems(completedItems)
                            .progress(progress)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MainChecklistDTO getMainChecklist(Long checklistId) {
        Long memberId = getMemberId();

        Optional<Checklist> target = (checklistId == null)
                ? checklistRepository.findTopByUserNoOrderByCreatedTimeDesc(memberId)
                : checklistRepository.findById(checklistId);

        if (target.isEmpty()) {
            return new MainChecklistDTO(null, null, Collections.emptyList());
        }

        Checklist checklist = target.get();

        if (!checklist.getUserNo().equals(memberId)) {
            throw new CustomException(ErrorCode.CHECKLIST_ACCESS_DENIED);
        }

        List<MainChecklistItemDTO> pendingItems = checklistItemRepository
                .findTop3ByChecklistIdAndCompletedFalseOrderByCreatedTimeDesc(checklist.getId())
                .stream()
                .map(MainChecklistItemDTO::new)
                .collect(Collectors.toList());

        return new MainChecklistDTO(checklist.getId(), checklist.getTitle(), pendingItems);
    }

    public List<MainArticleDTO> getMainArticles() {
        return articleRepository.findTop3ByOrderByViewsDesc().stream()
                .map(article -> new MainArticleDTO(
                        article.getTitle(),
                        article.getSummary(),
                        article.getThumbnail(),
                        article.getUrl(),
                        article.getViews()))
                .collect(Collectors.toList());
    }
}