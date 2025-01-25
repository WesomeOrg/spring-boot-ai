package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping ("/genericChatOptions")
    String chatWithGenericOptions(@RequestParam (defaultValue = "Hello, I am learning Ai with Spring") String message) {
        Prompt prompt = new Prompt(message, ChatOptions.builder().temperature(Double.valueOf(1.3f)).build());
        return this.chatClient.prompt(prompt).call().content();
    }


}