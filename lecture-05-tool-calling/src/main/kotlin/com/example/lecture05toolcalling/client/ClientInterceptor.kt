package com.example.lecture05toolcalling.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class WeatherApiAuthInterceptor(
    @Value("\${app.weather.api-key}") private val apiKey: String
) : ClientHttpRequestInterceptor {

    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {

        // Safely parse the original URI and append the query parameter
        val secureUri = UriComponentsBuilder.fromUri(request.uri)
            .queryParam("key", apiKey)
            .build(true) // true = keep existing encoding intact
            .toUri()

        // Create an anonymous wrapper to override the URI
        val modifiedRequest = object : HttpRequest by request {
            override fun getURI() = secureUri
        }

        return execution.execute(modifiedRequest, body)
    }
}