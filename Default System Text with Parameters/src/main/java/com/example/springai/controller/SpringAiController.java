package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAiController {
    @Autowired
    private ChatClient chatClient;

    @GetMapping("/defaultSystemText")
    String defaultSystemText(@RequestParam(value = "personality", defaultValue = "Pirate") String personality) {
        String helloPrompt = "Hello, I am learning Ai with Spring";
        return chatClient.prompt()
                .system(sp -> sp.param("personality", personality))
                .user(helloPrompt)
                .call()
                .content();
    }
}