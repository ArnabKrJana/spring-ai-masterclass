package com.example.lecture04ragvectorstore.controller

import com.example.lecture04ragvectorstore.model.dtos.Input
import com.example.lecture04ragvectorstore.model.dtos.Output
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.web.bind.annotation.*

@RequestMapping("/rag/chat")
@RestController
class ChatController(
    builder: ChatClient.Builder,
    chatMemory: ChatMemory,
    private val vectorStore: VectorStore
) {
    private val logger = LoggerFactory.getLogger(ChatController::class.java)

    private val chatClient = builder
        .defaultAdvisors(
            SimpleLoggerAdvisor(),
            // 1. Remembers previous chat history
            MessageChatMemoryAdvisor.builder(chatMemory).build(),
            // 2. Intercepts the query, searches VectorStore, and feeds context to the LLM
            QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.Builder().topK(15).build())
                .build()
        )
        .build()

    @GetMapping("/search")
    fun searchVectorStore(@RequestParam query: String): Output {
        logger.info("Query received: {}", query)
        logger.info("Start searching for '{}' in vector store...", query)

        val results = vectorStore.similaritySearch(query)
        val content = results.mapNotNull { it.text }.joinToString(separator = "\n\n")

        return Output(content)
    }

    @PostMapping
    fun chat(@Valid @RequestBody input: Input): Output {
        logger.info("Chat prompt received: {}", input.prompt)

        val response = chatClient.prompt()
            .user(input.prompt)
            // Pass the conversation ID from your DTO to the ChatMemory advisor
            .advisors { a ->
                a.param("chat_memory_conversation_id", input.conversationId)
            }
            .call()
            .content() ?: ""

        return Output(response)
    }
}