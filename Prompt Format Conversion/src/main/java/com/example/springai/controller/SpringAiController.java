package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/prompt")
    public String prompt() {
        var userMessageText = Map.of("abc", "abc@gmail.com", "def", "def@gmail.com", "ghi", "ghi@gmail.com");

        var systemMessageText = """
                Translate the following java map from JSON to an HTML table with column headers and title:
                ```{userMessageText}```
                """;

        var systemMessage = new SystemMessage(systemMessageText);
        var userMessage = new UserMessage(String.valueOf(userMessageText));
        var prompt = new Prompt(List.of(systemMessage, userMessage));
        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
    }
}