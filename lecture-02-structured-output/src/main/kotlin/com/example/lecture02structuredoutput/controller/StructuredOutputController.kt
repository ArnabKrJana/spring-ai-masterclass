package com.example.lecture02structuredoutput.controller

import com.example.lecture02structuredoutput.models.dtos.FilmRequest
import com.example.lecture02structuredoutput.models.dtos.FilmResponse
import com.example.lecture02structuredoutput.models.dtos.Output
import com.example.lecture02structuredoutput.models.dtos.TitleSuggestionRequest
import com.example.lecture02structuredoutput.models.dtos.TitleSuggestionResponse
import com.example.lecture02structuredoutput.models.dtos.YoutubeRequest
import com.example.lecture02structuredoutput.models.dtos.YoutubeResponse
import com.example.lecture02structuredoutput.models.dtos.YoutubeResponseWrapper
import io.micrometer.core.instrument.binder.http.Outcome
import jakarta.validation.Valid
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.converter.BeanOutputConverter
import org.springframework.ai.converter.ListOutputConverter
import org.springframework.ai.converter.MapOutputConverter
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/structured")
@RestController
class StructuredOutputController(
    builder: ChatClient.Builder
) {
    private val chatClient = builder
        .defaultAdvisors(SimpleLoggerAdvisor())
        .build()

    @PostMapping("/suggest-titles")
    fun suggestTitles(@Valid @RequestBody request: TitleSuggestionRequest): TitleSuggestionResponse {

        val outputConverter = ListOutputConverter()
        val promptTemplate: PromptTemplate = PromptTemplate(
            """
                I would like to upload a video on {platform} about the following:
                
                {topic}
                
                Give me {count} title suggestions for this topic.
                Make sure the title is relevant to the topic and it should be a single sort sentence.
                
                {format}
            """.trimIndent()
        )
        val additionalVariables = mapOf(
            "platform" to request.platform,
            "topic" to request.topic,
            "count" to request.count,
            "format" to outputConverter.format,
        )
        val message: Message = promptTemplate.createMessage(additionalVariables)
        val response = chatClient.prompt().messages(message).call().content() ?: ""
        val titleSuggestionResponse = outputConverter.convert(response)
        return TitleSuggestionResponse(titles = titleSuggestionResponse)
    }

    @PostMapping("/suggest-films")
    fun filmSuggestion(@Valid @RequestBody request: FilmRequest): FilmResponse {
        val outputConverter = MapOutputConverter()

        val promptTemplate = PromptTemplate(
            """
            You are an expert film archivist and cinephile.

            Your task is to recommend exactly {count} films directed by {director}.
            For each film, provide the title, release year, and a short, compelling one-sentence logline explaining why it is significant.

            Do not include any introductory or concluding conversational text.
            Strictly adhere to the following output format instructions:

            {format}
            """.trimIndent()
        )
        val additionalVariables = mapOf(
            "director" to request.director,
            "count" to request.count,
            "format" to outputConverter.format
        )
        val message: Message = promptTemplate.createMessage(additionalVariables)
        val response = chatClient.prompt().messages(message).call().content() ?: ""
        val filmResponse = outputConverter.convert(response)
        return FilmResponse(filmResponse)
    }

    @PostMapping("/gen-youtube-things")
    fun generateYoutubeThings(@Valid @RequestBody request: YoutubeRequest): YoutubeResponseWrapper {

        val outputConverter = BeanOutputConverter(YoutubeResponseWrapper::class.java)

        val promptTemplate = PromptTemplate(
            """
                You are an expert YouTube content strategist and SEO specialist.

                Generate exactly {itemCount} highly engaging video metadata concepts for the following topic:
                {topic}

                For each concept, you must provide: 
                1. title: A catchy, high-retention video title.
                2. description: An engaging, SEO-optimized description (2-3 sentences max).
                3. hashTags: Exactly {hashtagsCount} relevant, high-traffic hashtags.

                Do not include any introductory or concluding conversational text.
                Strictly adhere to the following JSON output format instructions:

                {format}
            """.trimIndent()
        )
        val additionalVariables = mapOf(
            "topic" to request.topic,
            "itemCount" to request.itemCount,
            "hashtagsCount" to request.hashtagsCount,
            "format" to outputConverter.format,
        )
        val message: Message = promptTemplate.createMessage(additionalVariables)
        val response = chatClient.prompt().messages(message).call().content() ?: ""
        val youtubeResponseWrapper = outputConverter.convert(response)
        return youtubeResponseWrapper ?: YoutubeResponseWrapper(ideas = emptyList())
    }
}