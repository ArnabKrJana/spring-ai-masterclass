package com.example.lecture05toolcalling.controller

import com.example.lecture05toolcalling.model.dtos.Input
import com.example.lecture05toolcalling.model.dtos.Output
import com.example.lecture05toolcalling.service.WeatherService
import com.example.lecture05toolcalling.tools.DateTimeTools
import jakarta.validation.Valid
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/chat")
@RestController
class ChatController(
    builder: ChatClient.Builder,
    weatherService: WeatherService,
    dateTimeTools: DateTimeTools,
    chatMemory: ChatMemory
) {

    private val chatClient = builder
        .defaultSystem(
            """
            You are a helpful assistant for the users.
            You always respond based on the data you have from tools available to you.
            If you don't know the answer, you will respond with "I don't know".
        """.trimIndent()
        )
        .defaultTools(weatherService, dateTimeTools)
        .defaultAdvisors(SimpleLoggerAdvisor(), MessageChatMemoryAdvisor.builder(chatMemory).build())
        .build()

    @PostMapping
    fun chat(@Valid @RequestBody input: Input): Output {
        // Pass the user's prompt to the LLM
        val response = chatClient.prompt(input.prompt)
            .call()
            .content() ?: "I don't know"

        return Output(response)
    }
}