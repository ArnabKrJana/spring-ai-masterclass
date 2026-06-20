package com.example.lecture03chatmemory.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ChatWebController {

    // Maps to localhost:8080/
    @GetMapping("/")
    fun chatPage(): String {
        // This tells Spring to look for src/main/resources/templates/chat.html
        return "chat"
    }
}