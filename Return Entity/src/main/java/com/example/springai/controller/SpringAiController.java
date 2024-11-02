package com.example.springai.controller;

import com.example.springai.controller.entity.Code;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/entity")
    Code entity() {
        String helloPrompt = "Generate a coding language and there detail.";
        return chatClient.prompt().user(helloPrompt).call().entity(Code.class);
    }
}