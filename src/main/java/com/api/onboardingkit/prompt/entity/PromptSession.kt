package com.api.onboardingkit.prompt.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "prompt_sessions")
class PromptSession(

    @Id
    @Column(name = "session_id")
    val id: String,

    val userNo: Long,

    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this("", 0L)
}