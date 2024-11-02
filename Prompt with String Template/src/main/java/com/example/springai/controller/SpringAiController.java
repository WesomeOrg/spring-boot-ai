package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;
    @Value("classpath:/prompts/prompt.st")
    private Resource systemResource;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/hello")
    String hello() {
        return this.chatClient.prompt().user(systemResource).call().content();
    }
}