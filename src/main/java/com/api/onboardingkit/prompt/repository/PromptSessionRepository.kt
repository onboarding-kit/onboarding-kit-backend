package com.api.onboardingkit.prompt.repository

import com.api.onboardingkit.prompt.entity.PromptSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PromptSessionRepository : JpaRepository<PromptSession, String> {
    fun findByUserNo(userNo: Long): List<PromptSession>
}