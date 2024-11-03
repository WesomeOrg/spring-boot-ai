package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAiController {
    @Autowired
    private ChatClient chatClient;

    @GetMapping("/defaultSystemText")
    String defaultSystemText() {
        String helloPrompt = "Hello, I am learning Ai with Spring";
        return this.chatClient.prompt().user(helloPrompt).call().content();

    }
}