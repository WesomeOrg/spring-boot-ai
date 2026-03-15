package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class SpringAiController {
    @Autowired
    private ChatClient chatClient;

    @GetMapping("/hello")
    String hello(@RequestParam(value = "prompt", defaultValue = "Hello, I am learning Ai with Spring") String prompt) {
        return chatClient.prompt().user(prompt).advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, UUID.randomUUID())).call().content();
    }
}