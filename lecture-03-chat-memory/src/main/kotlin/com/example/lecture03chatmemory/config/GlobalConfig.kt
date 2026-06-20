package com.example.lecture03chatmemory.config

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository
import org.springframework.beans.factory.annotation.Qualifier

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class GlobalConfig {


    @Bean
    fun providesMessageWindowChatMemory(jdbcChatMemoryRepository: JdbcChatMemoryRepository): MessageWindowChatMemory {
        val maxMessage = 100
        return MessageWindowChatMemory.builder()
            .chatMemoryRepository(jdbcChatMemoryRepository)
            .maxMessages(maxMessage)
            .build()
    }

    @Primary
    @Bean
    fun providesChatMemory(messageWindowChatMemory: MessageWindowChatMemory): ChatMemory {
        return messageWindowChatMemory
    }
}