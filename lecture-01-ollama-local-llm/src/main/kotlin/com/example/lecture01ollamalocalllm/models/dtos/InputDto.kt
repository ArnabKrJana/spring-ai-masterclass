package com.example.lecture01ollamalocalllm.models.dtos

import jakarta.validation.constraints.NotBlank

data class Input(
    @field:NotBlank
    val prompt: String
)
