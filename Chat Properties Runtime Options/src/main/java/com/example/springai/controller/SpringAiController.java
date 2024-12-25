package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fasterxml.jackson.core.JsonFactory.FORMAT_NAME_JSON;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.defaultOptions(OllamaOptions.builder()
                .withModel(OllamaModel.MISTRAL)
                .withTemperature(0.4)
                .withFormat(FORMAT_NAME_JSON)
                .withKeepAlive("5m")
                .build()).build();
    }

    @GetMapping("/hello")
    String hello() {
        String helloPrompt = "Hello, I am learning Ai with Spring";
        return this.chatClient.prompt().user(helloPrompt).call().content();
    }
}