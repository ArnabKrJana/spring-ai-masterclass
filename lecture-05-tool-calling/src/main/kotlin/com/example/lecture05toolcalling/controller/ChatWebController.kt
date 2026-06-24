package com.example.lecture05toolcalling.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ChatWebController {

    // Serves the HTML page when you navigate to http://localhost:8080/
    @GetMapping("/")
    fun chatPage(): String {
        return "chat"
    }
}