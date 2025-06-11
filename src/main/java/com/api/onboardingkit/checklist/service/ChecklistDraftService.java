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

    private final RedisTemplate<String, ChecklistDraft> checklistDraftRedisTemplate;

    private static final Duration TTL = Duration.ofMinutes(30);

    public String saveDraft(String sessionId, List<String> items) {
        String draftId = UUID.randomUUID().toString();
        ChecklistDraft draft = ChecklistDraft.of(sessionId, items);
        checklistDraftRedisTemplate.opsForValue().set(draftKey(draftId), draft, TTL);
        return draftId;
    }

    public ChecklistDraft getDraft(String draftId) {
        String key = draftKey(draftId);
        ChecklistDraft raw = checklistDraftRedisTemplate.opsForValue().get(key);
        if (raw == null) {
            throw new IllegalArgumentException("해당 임시 체크리스트가 존재하지 않습니다.");
        }
        return raw;
    }

    private String draftKey(String draftId) {
        return "checklist:draft:" + draftId;
    }

}
