package com.example.springai.controller;

import com.example.springai.configuration.ReReadingAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;


    public SpringAiController(ChatClient.Builder chatClient) {
        ReReadingAdvisor reReadingAdvisor=new ReReadingAdvisor();
        this.chatClient = chatClient.defaultSystem("You are a friendly chat bot that answers question in very detail with example in a story format.")
                .defaultAdvisors(reReadingAdvisor)
                .build();
    }

    @GetMapping("/hello")
    String hello(@RequestParam(value = "prompt", defaultValue = "Hello, I am learning Ai with Spring") String prompt) {
        return this.chatClient.prompt().user(prompt).call().content();
    }
}