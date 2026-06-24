package com.example.lecture05toolcalling.model.dtos


// The output return back to the LLM
data class Response(
    val location: String,
    val temperatureCelsius: Double,
    val feelsLikeCelsius: Double,
    val humidityPercent: Int,
    val condition: String
)

data class AstroResponse(
    val location: String,
    val sunrise: String,
    val sunset: String,
    val moonPhase: String
)