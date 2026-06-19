package com.example.lecture01ollamalocalllm.controller

import com.example.lecture01ollamalocalllm.models.dtos.Input
import com.example.lecture01ollamalocalllm.models.dtos.Output
import com.example.lecture01ollamalocalllm.models.dtos.TitleSuggestionRequest
import jakarta.validation.Valid
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.text.Charsets.UTF_8

@RestController
@RequestMapping("/api/chat/with-system-prompt")
class ChatControllerWithSystemPrompt(
    builder: ChatClient.Builder,
// Tell Spring to load the file from the resources/prompts folder
    @Value("classpath:/prompts/tweet-system-message.st")
    private val tweetSystemResource: Resource

) {
//    val chatClient = builder
//        .defaultAdvisors(SimpleLoggerAdvisor())
//        .build()

    val chatClient = builder
        .defaultAdvisors(SimpleLoggerAdvisor())
        .defaultSystem(tweetSystemResource)
        .build()

    @PostMapping
    fun chat(@Valid @RequestBody input: Input): Output {
        //define what the system is and how it should behave
//        val systemPrompt: String = "You are a professional assistant. You like to answer very much structured way, more of a book-ish way."
        val systemPrompt: String =
            "You are a cinephile assistant. That is why you would like to answer with cinema analogy."
        val systemMessage: SystemMessage = SystemMessage(systemPrompt)
        //define what the user is saying
        val userPrompt: String = input.prompt
        val userMessage: UserMessage = UserMessage(userPrompt)
        // create a custom prompt object
        val prompt: Prompt = Prompt(listOf(systemMessage, userMessage))
        //generate response by calling .call() and filter out only the content by calling content() and handle nullability
        val response = chatClient.prompt(prompt).call().content() ?: ""
        return Output(response)
    }


    @PostMapping("/suggest-titles")
    fun suggestTitles(@Valid @RequestBody request: TitleSuggestionRequest): Output {
        val promptTemplate: PromptTemplate = PromptTemplate(
            """
                I would like to upload a video on {platform} about the following:
                
                {topic}
                
                Give me {count} title suggestions for this topic.
                Make sure the title is relevant to the topic and it should be a single sort sentence.
            """.trimIndent()
        )
        val additionalVariables = mapOf(
            "platform" to request.platform,
            "topic" to request.topic,
            "count" to request.count
        )
        val message: Message = promptTemplate.createMessage(additionalVariables)
        val response = chatClient.prompt().messages(message).call().content() ?: ""
        return Output(response)
    }

    @PostMapping("/gen-tweet")
    fun generateTweet(@Valid @RequestBody input: Input): Output {
//        val systemPrompt: String = tweetSystemResource.getContentAsString(UTF_8)
//        val systemMessage: SystemMessage = SystemMessage(systemPrompt)
        val userMessage: UserMessage = UserMessage(input.prompt)
//        val prompt = Prompt(listOf(systemMessage, userMessage))
        val response = chatClient.prompt().messages(userMessage).call().content() ?: ""
        return Output(response)
    }
}