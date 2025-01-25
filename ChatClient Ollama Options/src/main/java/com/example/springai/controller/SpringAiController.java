package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/hello")
    String hello() {
        String message = "Hello, I am learning Ai with Spring";
        Prompt prompt = new Prompt(message, OllamaOptions.builder()
                .model(OllamaModel.LLAMA3_1)
                .frequencyPenalty(Double.valueOf(1.3))
                .presencePenalty(Double.valueOf(1.0))
                .stop(List.of("this-is-the-end", "done"))
                .temperature(Double.valueOf(0.7))
                .topP(Double.valueOf(0))
                .build());
        return this.chatClient.prompt(prompt).call().content();
    }
}