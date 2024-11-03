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
    public List<String> prompt() {
        List<String> language = List.of("The girl with the black and white puppies have a ball.",  // The girl has a ball.
                "Yolanda has her notebook.",// ok
                "Its going to be a long day. Does the car need it’s oil changed?",  // Homonyms
                "Their goes my freedom. There going to bring they’re suitcases.",  // Homonyms
                "Your going to need you’re notebook.",  // Homonyms
                "That medicine effects my ability to sleep. Have you heard of the butterfly affect?", // Homonyms
                "This phrase is to cherck chatGPT for speling abilitty"    // spelling
        );

        String systemMessageText = """
                Proofread and correct the following text delimited by triple backticks and rewrite the corrected version. If you don't find and errors, just say "No errors found". Don't use any punctuation around the text:
                ```{text}```
                """;
        var systemMessage = new SystemMessage(systemMessageText);
        return language.stream()
                .map(UserMessage::new)
                .map(userMessage -> new Prompt(List.of(systemMessage, userMessage)))
                .map(prompt1 -> chatClient.prompt(prompt1).call().chatResponse().getResult().getOutput().getContent())
                .toList();
    }
}