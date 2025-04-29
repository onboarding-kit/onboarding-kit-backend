package com.api.onboardingkit.prompt.dto

import java.time.LocalDateTime

data class PromptSessionResponseDTO(
    val id: String,
    val createdAt: LocalDateTime
)