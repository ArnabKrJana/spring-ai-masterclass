package com.example.lecture05toolcalling.model.dtos


import com.fasterxml.jackson.annotation.JsonProperty

// The root response object
data class WeatherApiResponse(
    val location: LocationData,
    val current: CurrentWeather
)

// Extracts just the location identifiers
data class LocationData(
    val name: String,
    val region: String,
    val country: String
)

// Extracts the core weather metrics
data class CurrentWeather(
    @JsonProperty("temp_c")
    val tempC: Double,

    @JsonProperty("feelslike_c")
    val feelslikeC: Double,

    val humidity: Int,

    val condition: WeatherCondition
)

// Extracts the text description (e.g., "Smoky haze")
data class WeatherCondition(
    val text: String
)

data class AstronomyApiResponse(
    val astronomy: AstronomyData
)

data class AstronomyData(
    val astro: AstroMetrics
)

data class AstroMetrics(
    val sunrise: String,
    val sunset: String,
    @JsonProperty("moon_phase")
    val moonPhase: String
)