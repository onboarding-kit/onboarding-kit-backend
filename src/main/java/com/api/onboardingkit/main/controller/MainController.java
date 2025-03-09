package com.api.onboardingkit.main.controller;

import com.api.onboardingkit.main.dto.MainStatusChecklistDTO;
import com.api.onboardingkit.main.dto.MainArticleDTO;
import com.api.onboardingkit.main.dto.MainChecklistDTO;
import com.api.onboardingkit.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping("/checklists/status")
    public ResponseEntity<List<MainStatusChecklistDTO>> getChecklistProgress(
    ) {
        return ResponseEntity.ok(mainService.getMainChecklistStatus());
    }

    @GetMapping("/checklists")
    public ResponseEntity<MainChecklistDTO> getMainChecklist() {
        return ResponseEntity.ok(mainService.getMainChecklist());
    }

    @GetMapping("/articles")
    public ResponseEntity<List<MainArticleDTO>> getMainArticles(
    ) {
        return ResponseEntity.ok(mainService.getMainArticles());
    }

}