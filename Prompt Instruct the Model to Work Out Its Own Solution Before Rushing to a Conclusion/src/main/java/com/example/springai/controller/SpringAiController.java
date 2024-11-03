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
        String userMessageText = """
                Determine if the student's solution is correct or not.

                Question:
                I'm building a solar power installation and I need help working out the financials.
                - Land costs $100 square foot
                - I can buy solar panels for $250 square foot
                - I negotiated a contract for maintenance that will cost me a flat $100k per year, and an additional $10 square foot.

                What is the total cost for the first year of operations as a function of the number of square feet.

                Student's Solution:
                Let x be the size of the installation in square feet.
                Costs:
                1. Land cost: 100x
                2. Solar panel cost: 250x
                3. Maintenance cost: 100,000 + 100x
                Total cost: 100x + 250x + 100,000 + 100x = 450x + 100,000
                """;
        var userMessage = new UserMessage(userMessageText);
        Prompt prompt = new Prompt(List.of(userMessage));
        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();
    }
}