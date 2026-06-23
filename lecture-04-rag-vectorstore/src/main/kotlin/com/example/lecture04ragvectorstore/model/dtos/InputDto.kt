package com.example.lecture04ragvectorstore.model.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Null
import java.util.UUID

data class Input(
    @field:NotBlank
    val prompt: String,
    @field:NotBlank
    val conversationId: String= UUID.randomUUID().toString()
)
