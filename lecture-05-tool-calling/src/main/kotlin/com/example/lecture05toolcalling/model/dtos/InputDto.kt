package com.example.lecture05toolcalling.model.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Null
import java.util.UUID

data class Input(
    @field:NotBlank
    val prompt: String,
    val conversationId: String?= UUID.randomUUID().toString()
)
