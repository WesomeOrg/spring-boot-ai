package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/prompt")
    public String prompt() {
        var systemMessageText = """
                Translate the following text to French and Spanish and English pirate:
                ```{userMessageText}```
                """;
        var userMessageText = """
                Hi, I would like to order a blender.
                """;
        var systemMessage = new SystemMessage(systemMessageText);
        var userMessage = new UserMessage(userMessageText);
        var prompt = new Prompt(List.of(systemMessage, userMessage));
        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
    }
}