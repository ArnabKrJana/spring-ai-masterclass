package com.example.lecture05toolcalling.config

import com.example.lecture05toolcalling.client.WeatherApiAuthInterceptor
import com.example.lecture05toolcalling.client.WeatherApiClient
import com.example.lecture05toolcalling.util.Constants.BASE_URL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class WeatherApiConfig(
    private val authInterceptor: WeatherApiAuthInterceptor
) {

    @Bean
    fun weatherApiClient(): WeatherApiClient {
        val restClient = RestClient.builder()
            .baseUrl(BASE_URL)
            .requestInterceptor(authInterceptor)
            .build()

        val adaptor = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adaptor).build()
        return factory.createClient(WeatherApiClient::class.java)
    }
}