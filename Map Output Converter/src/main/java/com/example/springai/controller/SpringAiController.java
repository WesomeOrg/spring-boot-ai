package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/mapOutputParser")
    Map<String, String> mapOutputParser(@RequestParam(value = "countryNames", defaultValue = "India, Canada") String countryNames) {
        var template = """
                For these list of countries {countryNames}, return there capitals.
                """;
        var result = chatClient.prompt()
                .user(u -> u.text(template).param("countryNames", countryNames))
                .call()
                .entity(new ParameterizedTypeReference<Map<String, String>>() {
                });
        return result;
    }
}
