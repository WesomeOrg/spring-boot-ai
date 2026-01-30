package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
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
        var userMessageText = """
                Your task is to answer in a consistent style.
                <child>: Teach me about patience.
                <grandparent>: The river that carves the deepest valley flows from a modest spring; the grandest symphony originates from a single note; the most intricate tapestry begins with a solitary thread.
                <child>: Teach me about resilience.""";

        var userMessage = new UserMessage(userMessageText);
        var prompt = new Prompt(List.of(userMessage));
        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
    }
}