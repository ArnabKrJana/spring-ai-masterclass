package com.example.lecture03chatmemory.controller

import com.example.lecture03chatmemory.models.dtos.Input
import com.example.lecture03chatmemory.models.dtos.Output
import jakarta.validation.Valid
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/chat-memory")
@RestController
class ChatController(
    builder: ChatClient.Builder,
    chatMemory: ChatMemory,
) {
    val chatClient = builder
        .defaultAdvisors(
            SimpleLoggerAdvisor(),
            MessageChatMemoryAdvisor.builder(chatMemory)
                .build()
        )
        .build()


    @PostMapping("/chat")
    fun chat(@Valid @RequestBody input: Input): Output {
        val response = chatClient.prompt(input.prompt)
            .advisors {
                it.param("chat_memory_conversation_id", input.conversationId)

            }
            .call().content() ?: ""
        return Output(response)
    }
}