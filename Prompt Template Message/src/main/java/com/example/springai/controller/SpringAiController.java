package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/hello")
    String hello(@RequestParam(value = "topic", required = false, defaultValue = "Ai with Spring") String topic) {
        Message userMessage = new UserMessage("Hello, I am learning {topic}.");
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate("You are a friendly chat bot that answers question in very detail with example in a story format.");
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("topic", topic));
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        return chatClient.prompt(prompt).call().content();
    }
}