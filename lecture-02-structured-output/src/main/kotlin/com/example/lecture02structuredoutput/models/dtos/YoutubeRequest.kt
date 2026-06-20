package com.example.lecture02structuredoutput.models.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class YoutubeRequest(
    @field:NotNull
    val itemCount: Int,
    @field:NotNull
    val hashtagsCount: Int,
    @field:NotBlank
    val topic: String
)
