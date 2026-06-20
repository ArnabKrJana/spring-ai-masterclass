package com.example.lecture03chatmemory

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.TimeZone.*

@SpringBootApplication
class Lecture03ChatMemoryApplication

fun main(args: Array<String>) {
    setDefault(getTimeZone("Asia/Kolkata"))
    runApplication<Lecture03ChatMemoryApplication>(*args)
}
