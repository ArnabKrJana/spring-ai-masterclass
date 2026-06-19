package com.example.lecture01ollamalocalllm.controller

import com.example.lecture01ollamalocalllm.models.dtos.Input
import com.example.lecture01ollamalocalllm.models.dtos.Output
import jakarta.validation.Valid
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/")
@RestController
class ChatController(
    builder: ChatClient.Builder
) {
    private val chatClient: ChatClient = builder
        .defaultAdvisors(SimpleLoggerAdvisor())
        .build()

    @PostMapping("/api/chat")
    fun chat(@Valid @RequestBody input: Input): Output {
// .content() returns a nullable String in Kotlin, so we can default to an empty string if null
        val response = chatClient.prompt(input.prompt).call().content() ?: ""
        return Output(response)
    }
}

