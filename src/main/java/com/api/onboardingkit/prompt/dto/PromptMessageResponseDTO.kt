package com.api.onboardingkit.prompt.dto

import java.time.LocalDateTime

data class PromptMessageResponseDTO(
    val messageText: String,
    val isUser: Boolean,
    val timestamp: LocalDateTime
)