package com.api.onboardingkit.prompt.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "prompt_message")
data class PromptMessage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "session_id", nullable = false)
    val sessionId: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val messageText: String,

    @Column(nullable = false)
    val isUser: Boolean,

    @Column(nullable = false, updatable = false)
    val timestamp: LocalDateTime = LocalDateTime.now()
)