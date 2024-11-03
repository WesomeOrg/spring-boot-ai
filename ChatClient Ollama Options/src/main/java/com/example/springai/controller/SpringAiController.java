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
                .withModel(OllamaModel.LLAMA3_1)
                .withFrequencyPenalty(1.3f)
                .withPresencePenalty(1.0f)
                .withStop(List.of("this-is-the-end", "done"))
                .withTemperature(0.7f)
                .withTopP(0f)
                .build());
        return this.chatClient.prompt(prompt).call().content();
    }
}