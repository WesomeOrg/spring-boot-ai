package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/hello")
    String hello(@RequestParam(value = "topic", required = false, defaultValue = "Ai with Spring") String topic) {
        PromptTemplate promptTemplate = new PromptTemplate("Hello, I am learning {topic}");
        Prompt prompt = promptTemplate.create(Map.of("topic", topic));
        return this.chatClient.prompt(prompt).call().content();
    }
}