package com.example.lecture05toolcalling.tools

import org.slf4j.LoggerFactory
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class DateTimeTools {
    private val logger = LoggerFactory.getLogger(DateTimeTools::class.java)

    @Tool(description = "Get the current date in the ISO-8601 format yyyy-MM-dd")
    fun getCurrentDate(): String{
        return LocalDate.now().toString()
    }
}