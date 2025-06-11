package com.api.onboardingkit.prompt.service

import com.api.onboardingkit.checklist.service.ChecklistDraftService
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
import io.github.cdimascio.dotenv.Dotenv
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class PromptService(
    private val promptSessionRepository: PromptSessionRepository,
    private val promptMessageRepository: PromptMessageRepository,
    private val checklistDraftService: ChecklistDraftService,
    private val restTemplate: RestTemplate
) : AbstractService() {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)
    private val OPENAI_API_URL = "https://api.openai.com/v1/chat/completions"
    private val dotenv = Dotenv.load()
    private val OPENAI_API_KEY = dotenv["OPENAI_API_KEY"]
        ?: throw IllegalStateException("OPENAI_API_KEY 환경 변수가 .env에 없습니다.")

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
            id = gptSessionId,
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

        // 1. 사용자 메시지 저장
        val userMessageEntity = PromptMessage(
            sessionId = session.id,
            messageText = userMessage,
            isUser = true,
            timestamp = LocalDateTime.now()
        )
        promptMessageRepository.save(userMessageEntity)

        // 2. 메시지에 "체크리스트"가 포함되어 있으면 GPT에게 명시적으로 요청
        val shouldIntercept = userMessage.contains("체크리스트")
        val messageForGpt = if (shouldIntercept) {
            "$userMessage\n\n체크리스트는 HTML <ul><li> 태그로 구성해줘."
        } else userMessage

        val gptResponse = callGptApi(sessionId, messageForGpt)

        // 3. 체크리스트 응답이면 가로채서 Redis 저장
        val checklistItems = parseChecklistItemsFromHtml(gptResponse)

        val finalMessageText = if (shouldIntercept && checklistItems.isNotEmpty()) {
            val draftId = checklistDraftService.saveDraft(sessionId, checklistItems)
            "/checklists/drafts/$draftId"
        } else {
            gptResponse
        }

        // 4. GPT 메시지 저장
        val botMessageEntity = PromptMessage(
            sessionId = session.id,
            messageText = finalMessageText,
            isUser = false,
            timestamp = LocalDateTime.now()
        )
        return promptMessageRepository.save(botMessageEntity)
    }

    fun parseChecklistItemsFromHtml(html: String): List<String> {
        val regex = Regex("""<li>(.*?)</li>""", RegexOption.DOT_MATCHES_ALL)
        return regex.findAll(html).map { it.groupValues[1].trim() }.toList()
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