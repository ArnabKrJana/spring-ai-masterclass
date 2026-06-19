package com.example.lecture02structuredoutput.models.dtos

data class FilmResponse(
val movies: Map<String, Any> = emptyMap()
)
