package com.api.onboardingkit.main.controller;

import com.api.onboardingkit.config.response.dto.CustomResponse;
import com.api.onboardingkit.config.response.dto.SuccessStatus;
import com.api.onboardingkit.main.dto.MainArticleDTO;
import com.api.onboardingkit.main.dto.MainChecklistDTO;
import com.api.onboardingkit.main.dto.MainStatusChecklistDTO;
import com.api.onboardingkit.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping("/checklists/status")
    public CustomResponse<?> getChecklistProgress() {
        return CustomResponse.success(mainService.getMainChecklistStatus(), SuccessStatus.SUCCESS);
    }

    @GetMapping({"/checklists", "/checklists/{checklistId}"})
    public CustomResponse<?> getMainChecklist(
            @PathVariable(value = "checklistId", required = false) Long checklistId
    ) {
        return CustomResponse.success(mainService.getMainChecklist(checklistId), SuccessStatus.SUCCESS);
    }

    @GetMapping("/articles")
    public CustomResponse<?> getMainArticles() {
        return CustomResponse.success(mainService.getMainArticles(), SuccessStatus.SUCCESS);
    }
}