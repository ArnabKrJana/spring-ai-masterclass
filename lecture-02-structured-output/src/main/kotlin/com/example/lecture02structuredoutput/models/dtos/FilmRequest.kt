package com.example.lecture02structuredoutput.models.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class FilmRequest(
    @field:NotBlank
    val director: String,
    @field:NotNull
    val count:Int
)
