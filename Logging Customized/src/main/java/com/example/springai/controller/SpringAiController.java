package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        var simpleLoggerAdvisor = new SimpleLoggerAdvisor(request -> "Custom request: " + request.userText(), response -> "Custom response: " + response.getResult());
        this.chatClient = chatClient.defaultAdvisors(simpleLoggerAdvisor).build();
    }

    @GetMapping("/hello")
    String hello(@RequestParam(value = "prompt", defaultValue = "Hello, I am learning Ai with Spring.") String prompt) {
        return chatClient.prompt().user(prompt).call().content();
    }
}