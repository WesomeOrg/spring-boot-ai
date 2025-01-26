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
        var systemMessageText = """
                Identify the following items from the review text:
                - Item purchased by reviewer
                - Company that made the item

                The review is delimited with triple backticks. Format your response as a JSON object with "Item" and "Brand" as the keys.
                If the information isn't present, use "unknown" as the value.
                Make your response as short as possible.

                Review:
                ```{userMessageText}```
                """;
        var userMessageText = """
                Needed a nice lamp for my bedroom, and this one had additional storage and not too high of a price point. Got it fast.  The string to our lamp broke during the transit and the company happily sent over a new one. Came within a few days as well. It was easy to put together.  I had a missing part, so I contacted their support and they very quickly got me the missing piece! Lumina seems to me to be a great company that cares about their customers and products!!
                """;

        var systemMessage = new SystemMessage(systemMessageText);
        var userMessage = new UserMessage(userMessageText);
        var prompt = new Prompt(List.of(systemMessage, userMessage));
        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();

    }
}