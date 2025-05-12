package com.api.onboardingkit.prompt.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class PromptMessageResponseDTO(
    val messageText: String,
    @JsonProperty("isUser")
    val isUser: Boolean,
    val timestamp: LocalDateTime
)
