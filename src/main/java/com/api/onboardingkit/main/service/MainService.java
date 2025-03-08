package com.api.onboardingkit.main.service;

import com.api.onboardingkit.article.repository.ArticleRepository;
import com.api.onboardingkit.checklist.entity.Checklist;
import com.api.onboardingkit.checklist.repository.ChecklistItemRepository;
import com.api.onboardingkit.checklist.repository.ChecklistRepository;
import com.api.onboardingkit.config.SecurityUtil;
import com.api.onboardingkit.main.dto.MainArticleDTO;
import com.api.onboardingkit.main.dto.MainChecklistItemDTO;
import com.api.onboardingkit.main.dto.MainStatusChecklistDTO;
import com.api.onboardingkit.main.dto.MainChecklistDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MainService {

    private final ArticleRepository articleRepository;
    private final ChecklistRepository checklistRepository;
    private final ChecklistItemRepository checklistItemRepository;

    public List<MainStatusChecklistDTO> getMainChecklistStatus() {
        Long userNo = SecurityUtil.getCurrentUserNo();
        return checklistRepository.findByUserNo(userNo).stream()
                .map(checklist -> {
                    Integer totalItems = checklistItemRepository.countByChecklistId(checklist.getId());
                    Integer completedItems = checklistItemRepository.countByChecklistIdAndCompleted(checklist.getId(), true);
                    double progress = totalItems > 0 ? (completedItems * 100.0 / totalItems) : 0.0;

                    return new MainStatusChecklistDTO(checklist.getTitle(), totalItems, completedItems, progress);
                })
                .collect(Collectors.toList());
    }

    public MainChecklistDTO getMainChecklist() {
        Long userNo = SecurityUtil.getCurrentUserNo();

        Checklist recentChecklist = checklistRepository.findTopByUserNoOrderByCreatedTimeDesc(userNo)
                .orElseThrow(() -> new IllegalArgumentException("최근 체크리스트가 존재하지 않습니다."));

        List<MainChecklistItemDTO> pendingItems = checklistItemRepository
                .findTop3ByChecklistIdAndCompletedFalseOrderByCreatedTimeDesc(recentChecklist.getId())
                .stream()
                .map(MainChecklistItemDTO::new)
                .collect(Collectors.toList());

        return new MainChecklistDTO(recentChecklist.getId(), recentChecklist.getTitle(), pendingItems);
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