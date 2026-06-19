package com.example.lecture02structuredoutput.models.dtos

import jakarta.validation.constraints.NotBlank

data class Input(
    @field:NotBlank
    val prompt: String
)
