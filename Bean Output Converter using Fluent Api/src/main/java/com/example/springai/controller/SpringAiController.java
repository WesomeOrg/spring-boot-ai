package com.example.springai.controller;

import com.example.springai.entity.Country;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/beanOutputParser")
    Country hello(@RequestParam(value = "letter", defaultValue = "a") String letter) {
        var prompt = "Give me a country starts with {letter} and its capital.";
        var actorsFilms = chatClient.prompt()
                .user(u -> u.text(prompt).param("letter", letter))
                .call()
                .entity(Country.class);
        return actorsFilms;
    }
}
