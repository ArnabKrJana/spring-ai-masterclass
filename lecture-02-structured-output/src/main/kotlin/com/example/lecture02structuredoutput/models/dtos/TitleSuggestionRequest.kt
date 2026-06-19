package com.example.lecture02structuredoutput.models.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.ai.converter.ListOutputConverter

data class TitleSuggestionRequest(
    @field:NotBlank
    val platform: String,
    @field:NotBlank
    val topic: String,
    @field:NotNull
    val count: Int,

)
