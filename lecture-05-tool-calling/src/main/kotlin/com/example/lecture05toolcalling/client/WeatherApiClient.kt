package com.example.lecture05toolcalling.client

import com.example.lecture05toolcalling.model.dtos.AstronomyApiResponse
import com.example.lecture05toolcalling.model.dtos.WeatherApiResponse
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

interface WeatherApiClient {
   // Matches: https://api.weatherapi.com/v1/current.json?key=YOUR_KEY&q=LOCATION
    @GetExchange("/current.json")
    fun getCurrentWeather(
        @RequestParam("q") location: String
    ): WeatherApiResponse?

    @GetExchange("/astronomy.json")
    fun getAstronomy(
        @RequestParam("q") location: String,
        @RequestParam("dt") date: String
    ): AstronomyApiResponse?
}