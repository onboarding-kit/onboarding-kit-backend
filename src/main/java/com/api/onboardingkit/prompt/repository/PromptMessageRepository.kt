package com.api.onboardingkit.prompt.repository

import com.api.onboardingkit.prompt.entity.PromptMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PromptMessageRepository : JpaRepository<PromptMessage, Long> {
    fun findBySessionId(sessionId: String): List<PromptMessage>
}