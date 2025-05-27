package com.api.onboardingkit.prompt.controller

import com.api.onboardingkit.config.response.dto.CustomResponse
import com.api.onboardingkit.config.response.dto.SuccessStatus
import com.api.onboardingkit.prompt.dto.PromptMessageRequestDTO
import com.api.onboardingkit.prompt.dto.PromptMessageResponseDTO
import com.api.onboardingkit.prompt.dto.PromptSessionResponseDTO
import com.api.onboardingkit.prompt.service.PromptService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/prompt")
class PromptController(
    private val promptService: PromptService
) {

    @PostMapping("/session")
    fun createSession(): CustomResponse<PromptSessionResponseDTO> {
        val session = promptService.createSession()
        val response = PromptSessionResponseDTO(
            id = session.id,
            createdAt = session.createdAt
        )
        return CustomResponse.success(response, SuccessStatus.SUCCESS)
    }

    @GetMapping("/{sessionId}/messages")
    fun getMessages(@PathVariable sessionId: String): CustomResponse<List<PromptMessageResponseDTO>> {
        val messages = promptService.getMessages(sessionId).map { msg ->
            PromptMessageResponseDTO(
                messageText = msg.messageText,
                isUser = msg.isUser,
                timestamp = msg.timestamp
            )
        }
        return CustomResponse.success(messages, SuccessStatus.SUCCESS)
    }

    @PostMapping("/{sessionId}/messages")
    fun sendMessage(
        @PathVariable sessionId: String,
        @RequestBody request: PromptMessageRequestDTO
    ): CustomResponse<PromptMessageResponseDTO> {
        val botMessage = promptService.sendMessage(sessionId, request.message)

        val response = PromptMessageResponseDTO(
            messageText = botMessage.messageText,
            isUser = botMessage.isUser,
            timestamp = botMessage.timestamp
        )
        return CustomResponse.success(response, SuccessStatus.SUCCESS)
    }
}
