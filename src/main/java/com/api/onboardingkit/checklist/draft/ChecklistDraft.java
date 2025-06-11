package com.api.onboardingkit.checklist.draft;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistDraft implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String sessionId;
    private List<String> items;
    private LocalDateTime createdAt;

    public static ChecklistDraft of(String sessionId, List<String> items) {
        return new ChecklistDraft(sessionId, items, LocalDateTime.now());
    }
}
