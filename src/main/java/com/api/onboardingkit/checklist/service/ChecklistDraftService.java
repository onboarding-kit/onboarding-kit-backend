package com.api.onboardingkit.checklist.service;

import com.api.onboardingkit.checklist.draft.ChecklistDraft;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChecklistDraftService {

    private final RedisTemplate<String, Object> checklistDraftRedisTemplate;

    private static final Duration TTL = Duration.ofMinutes(30);

    public String saveDraft(String sessionId, List<String> items) {
        String draftId = UUID.randomUUID().toString();
        ChecklistDraft draft = ChecklistDraft.of(sessionId, items);
        checklistDraftRedisTemplate.opsForValue().set(draftKey(draftId), draft, TTL);
        return draftId;
    }

    public ChecklistDraft getDraft(String draftId) {
        Object raw = checklistDraftRedisTemplate.opsForValue().get(draftKey(draftId));
        if (raw == null || !(raw instanceof ChecklistDraft)) {
            throw new IllegalArgumentException("해당 임시 체크리스트가 존재하지 않습니다.");
        }
        return (ChecklistDraft) raw;
    }

    public void deleteDraft(String draftId) {
        checklistDraftRedisTemplate.delete(draftKey(draftId));
    }

    private String draftKey(String draftId) {
        return "checklist:draft:" + draftId;
    }

}
