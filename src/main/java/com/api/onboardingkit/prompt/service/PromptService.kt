package com.api.onboardingkit.prompt.service

import com.api.onboardingkit.config.AbstractService
import com.api.onboardingkit.prompt.entity.PromptMessage
import com.api.onboardingkit.prompt.entity.PromptSession
import com.api.onboardingkit.prompt.repository.PromptMessageRepository
import com.api.onboardingkit.prompt.repository.PromptSessionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.http.*
import java.time.LocalDateTime

@Service
class PromptService(
    private val promptSessionRepository: PromptSessionRepository,
    private val promptMessageRepository: PromptMessageRepository,
    private val restTemplate: RestTemplate
) : AbstractService() {

    private val OPENAI_API_URL = "https://api.openai.com/v1/chat/completions"
    private val OPENAI_API_KEY = "OPENAI_API_KEY"

    @Transactional
    fun createSession(): PromptSession {
        val gptSessionId: String = requestGptSessionId()

        if (gptSessionId.isBlank()) {
            throw IllegalStateException("GPT에서 유효한 세션 ID를 받아오지 못했습니다.")
        }

        val existingSession = promptSessionRepository.findById(gptSessionId)
        if (existingSession.isPresent) {
            return existingSession.get()
        }

        val session = PromptSession(
            id = gptSessionId,  // 🔹 `sessionId` → `id` 변경
            userNo = getMemberId()
        )

        return promptSessionRepository.save(session)
    }

    fun getMessages(sessionId: String): List<PromptMessage> {
        val session = promptSessionRepository.findById(sessionId)
            .orElseThrow { IllegalArgumentException("세션을 찾을 수 없습니다.") }

        return promptMessageRepository.findBySessionId(sessionId)
    }

    @Transactional
    fun sendMessage(sessionId: String, userMessage: String): PromptMessage {
        val session = promptSessionRepository.findById(sessionId)
            .orElseThrow { IllegalArgumentException("세션을 찾을 수 없습니다.") }

        val userMessageEntity = PromptMessage(
            sessionId = session.id,
            messageText = userMessage,
            isUser = true,
            timestamp = LocalDateTime.now()
        )
        promptMessageRepository.save(userMessageEntity)

        val gptResponse = callGptApi(sessionId, userMessage)

        val botMessageEntity = PromptMessage(
            sessionId = session.id,
            messageText = gptResponse,
            isUser = false,
            timestamp = LocalDateTime.now()
        )
        return promptMessageRepository.save(botMessageEntity)
    }

    private fun requestGptSessionId(): String {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "Bearer $OPENAI_API_KEY")
        }

        val requestBody = mapOf(
            "model" to "gpt-4",
            "messages" to listOf(mapOf("role" to "system", "content" to "Start a new session."))
        )

        val requestEntity = HttpEntity(requestBody, headers)

        val response = restTemplate.exchange(
            OPENAI_API_URL,
            HttpMethod.POST,
            requestEntity,
            Map::class.java
        )

        val sessionId = response.body?.get("id") as? String

        return sessionId ?: throw IllegalStateException("GPT에서 세션 ID를 받아올 수 없습니다.")
    }

    private fun callGptApi(sessionId: String, userMessage: String): String {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "Bearer $OPENAI_API_KEY")
        }

        val requestBody = mapOf(
            "model" to "gpt-4",
            "messages" to listOf(
                mapOf("role" to "system", "content" to "You are a helpful assistant."),
                mapOf("role" to "user", "content" to userMessage)
            )
        )

        val requestEntity = HttpEntity(requestBody, headers)

        val response = restTemplate.exchange(
            OPENAI_API_URL,
            HttpMethod.POST,
            requestEntity,
            Map::class.java
        )

        val choices = response.body?.get("choices") as? List<Map<String, Any>>
        val message = choices?.firstOrNull()?.get("message") as? Map<String, String>

        return message?.get("content") ?: "GPT 응답을 가져올 수 없습니다."
    }

}