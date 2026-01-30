package com.example.springai.controller;

import com.example.springai.tools.SpringAiTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/currentDateTime")
    String getCurrentDateTime() {
        var currentDateTimePrompt = "what is the current time ?";
        return this.chatClient.prompt().user(currentDateTimePrompt).call().content();
    }

    @GetMapping("/currentDateTimeWithTool")
    String getCurrentDateTimeWithTool() {
        var currentDateTimePrompt = "what is the current time ?";
        return this.chatClient.prompt().user(currentDateTimePrompt).tools(new SpringAiTools()).call().content();
    }
}
