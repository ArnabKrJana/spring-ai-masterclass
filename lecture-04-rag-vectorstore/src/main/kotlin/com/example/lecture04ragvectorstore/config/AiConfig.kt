package com.example.lecture04ragvectorstore.config

import org.slf4j.LoggerFactory
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.reader.markdown.MarkdownDocumentReader
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.SimpleVectorStore
import org.springframework.ai.vectorstore.VectorStore

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.io.Resource
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class AiConfig(
    @Value("classpath:/data/arnab_profile.md")
    private val profileResource: Resource,
    @Value("classpath:/data/resume.md")
    private val resume: Resource
) {
    private val logger = LoggerFactory.getLogger(AiConfig::class.java)

//    @Bean
//    fun providesVectorStore(jdbcTemplate: JdbcTemplate, embeddingModel: EmbeddingModel): VectorStore {
////        return SimpleVectorStore.builder(embeddingModel).build()
//        return PgVectorStore.builder(jdbcTemplate, embeddingModel).build()
//    }


    @Bean
    fun providesApplicationRunner(vectorStore: VectorStore): ApplicationRunner {
        return ApplicationRunner {
            loadDocs(vectorStore, profileResource)
            loadDocs(vectorStore, resume)
        }
    }

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

    private fun loadDocs(vectorStore: VectorStore, resource: Resource) {
        logger.info("Loading documents {} into vector store", resource.filename)

        val markdownDocsReader = MarkdownDocumentReader(resource, MarkdownDocumentReaderConfig.defaultConfig())
        val documents = markdownDocsReader.get()
        val textSplitter = TokenTextSplitter.builder().build()
        val splitDocuments = textSplitter.split(documents)
        vectorStore.accept(splitDocuments)
        logger.info("Documents {} loaded into vector store", resource.filename)

    }

    @Bean
    fun providesJedisConnectionFactory(): JedisConnectionFactory {
        return JedisConnectionFactory()
    }

}