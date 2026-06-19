package com.example.lecture01ollamalocalllm.models.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class TitleSuggestionRequest(
    @field:NotBlank
    val platform: String,
    @field:NotBlank
    val topic: String,
    @field:NotNull
    val count: Int
)
