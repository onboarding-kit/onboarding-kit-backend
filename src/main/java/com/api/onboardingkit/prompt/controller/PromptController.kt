package com.api.onboardingkit.prompt.controller

import com.api.onboardingkit.prompt.dto.PromptMessageRequestDTO
import com.api.onboardingkit.prompt.dto.PromptMessageResponseDTO
import com.api.onboardingkit.prompt.dto.PromptSessionResponseDTO
import com.api.onboardingkit.prompt.service.PromptService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/prompt")
class PromptController(
    private val promptService: PromptService
) {

    @PostMapping("/session")
    fun createSession(): ResponseEntity<PromptSessionResponseDTO> {
        val session = promptService.createSession()
        return ResponseEntity.ok(
            PromptSessionResponseDTO(
                id = session.id,
                createdAt = session.createdAt
            )
        )
    }

    @GetMapping("/{sessionId}/messages")
    fun getMessages(@PathVariable sessionId: String): ResponseEntity<List<PromptMessageResponseDTO>> {
        val messages = promptService.getMessages(sessionId).map { msg ->
            PromptMessageResponseDTO(
                messageText = msg.messageText,
                isUser = msg.isUser,
                timestamp = msg.timestamp
            )
        }
        return ResponseEntity.ok(messages)
    }

    @PostMapping("/{sessionId}/messages")
    fun sendMessage(
        @PathVariable sessionId: String,
        @RequestBody request: PromptMessageRequestDTO
    ): ResponseEntity<PromptMessageResponseDTO> {
        val botMessage = promptService.sendMessage(sessionId, request.message)

        return ResponseEntity.ok(
            PromptMessageResponseDTO(
                messageText = botMessage.messageText,
                isUser = botMessage.isUser,
                timestamp = botMessage.timestamp
            )
        )
    }
}