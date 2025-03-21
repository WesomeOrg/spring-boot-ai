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
                Your task is to help a marketing team create a description for a retail website of a product based on a technical fact sheet.

                Write a product description based on the information provided in the technical specifications delimited by triple backticks.

                Technical specifications:
                ```{userMessageText}```
                """;
        var userMessageText = """
                OVERVIEW
                - Part of a beautiful family of mid-century inspired office furniture, including filing cabinets, desks, bookcases, meeting tables, and more.
                - Several options of shell color and base finishes.
                - Available with plastic back and front upholstery (SWC-100) or full upholstery (SWC-110) in 10 fabric and 6 leather options.
                - Base finish options are: stainless steel, matte black, gloss white, or chrome.
                - Chair is available with or without armrests.
                - Suitable for home or business settings.
                - Qualified for contract use.

                CONSTRUCTION
                - 5-wheel plastic coated aluminum base.
                - Pneumatic chair adjust for easy raise/lower action.

                DIMENSIONS
                - WIDTH 53 CM | 20.87”
                - DEPTH 51 CM | 20.08”
                - HEIGHT 80 CM | 31.50”
                - SEAT HEIGHT 44 CM | 17.32”
                - SEAT DEPTH 41 CM | 16.14”

                OPTIONS
                - Soft or hard-floor caster options.
                - Two choices of seat foam densities: medium (1.8 lb/ft3) or high (2.8 lb/ft3)
                - Armless or 8 position PU armrests

                MATERIALS
                SHELL BASE GLIDER
                - Cast Aluminum with modified nylon PA6/PA66 coating.
                - Shell thickness: 10 mm.
                SEAT
                - HD36 foam

                COUNTRY OF ORIGIN
                - Italy
                """;
        var systemMessage = new SystemMessage(systemMessageText);
        var userMessage = new UserMessage(userMessageText);
        var prompt = new Prompt(List.of(systemMessage, userMessage));
        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
    }
}