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
    public String prompt() {
        String systemMessageText = """
                Perform the following actions:
                1 - Summarize the following text delimited by triple backticks with 1 sentence.
                2 - Translate the summary into French.
                3 - List each name in the French summary.
                4 - Output a json object that contains the following keys: french_summary, num_names.

                Separate your answers with line breaks.

                Text:
                ```{systemMessageText}```
                """;

        String userMessageText = """
                In a charming village, siblings Jack and Jill set out on a quest to fetch water from a hilltop well. As they climbed, singing joyfully, misfortune struck-Jack tripped on a stone and tumbled down the hill, with Jill following suit.
                Though slightly battered, the pair returned home to comforting embraces.
                Despite the mishap, their adventurous spirits remained undimmed, and they continued exploring with delight.
                """;

        var systemMessage = new SystemMessage(systemMessageText);
        var userMessage = new UserMessage(userMessageText);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
    }
}