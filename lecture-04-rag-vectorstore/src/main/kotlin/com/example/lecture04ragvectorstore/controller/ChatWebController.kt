package com.example.lecture04ragvectorstore.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ChatWebController {

    // Maps the root URL (http://localhost:8080/) to the chat UI
    @GetMapping("/")
    fun chatPage(): String {
        // Looks for src/main/resources/templates/chat.html
        return "chat"
    }
}