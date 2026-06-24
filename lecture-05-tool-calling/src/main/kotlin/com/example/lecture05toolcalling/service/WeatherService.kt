package com.example.lecture05toolcalling.service

import com.example.lecture05toolcalling.client.WeatherApiClient
import com.example.lecture05toolcalling.model.dtos.AstroResponse
import com.example.lecture05toolcalling.model.dtos.Response
import org.slf4j.LoggerFactory
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service

@Service
class WeatherService(
    private val client: WeatherApiClient,
) {
    private val logger = LoggerFactory.getLogger(WeatherService::class.java)

    @Tool(
        description = "Get the current real-time weather conditions for a specific city or location"
    )
    fun getWeather(location: String): Response {
        logger.info("LLM explicitly requested weather execution for: {}", location)
        var locName = location
        var temperature = 0.0
        var feelsLike = 0.0
        var humidity = 0
        var conditionText = "Unknown"
        try {
            logger.info("Getting weather for location: {} from weatherapi.com..", location)
            val apiResponse = client.getCurrentWeather(location)

            locName = apiResponse?.location?.name ?: location
            temperature = apiResponse?.current?.tempC ?: 0.0
            feelsLike = apiResponse?.current?.feelslikeC ?: 0.0
            humidity = apiResponse?.current?.humidity ?: 0
            conditionText = apiResponse?.current?.condition?.text ?: "Unknown"
            logger.info(conditionText)
        } catch (e: Exception) {
            logger.error("Error getting weather conditions for $location", e)
        }
        logger.info("returning weather for location: {}", locName)
        return Response(locName, temperature, feelsLike, humidity, conditionText)
    }


    @Tool(description = "Get the astronomical data including sunrise, sunset, and moon phase for a specific location and date in yyyy-MM-dd format")
    fun getAstronomy(location: String, date: String): AstroResponse {
        logger.info(" LLM explicitly requested astronomy data for: {} on {}", location, date)

        return try {
            val apiResponse = client.getAstronomy(location, date)
            val astro = apiResponse?.astronomy?.astro

            AstroResponse(
                location = location,
                sunrise = astro?.sunrise ?: "Unknown",
                sunset = astro?.sunset ?: "Unknown",
                moonPhase = astro?.moonPhase ?: "Unknown"
            )
        } catch (e: Exception) {
            logger.error("Error getting astronomy conditions for $location", e)
            AstroResponse(location, "Error", "Error", "Error")
        }
    }
}