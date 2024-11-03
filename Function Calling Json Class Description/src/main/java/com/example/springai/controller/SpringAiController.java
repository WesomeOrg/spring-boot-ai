package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/function")
    public String function(@RequestParam(value = "question") String question) {
        SystemMessage systemMessage = new SystemMessage("you are a helpful AI assistant answering question about cities around the world and its current weather in very detailed.");
        UserMessage userMessage = new UserMessage(question);
        OllamaOptions currentWeatherFunction = OllamaOptions.builder().withFunction("currentWeatherFunction").build();
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage), currentWeatherFunction);
        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();
        return chatResponse.getResult().getOutput().getContent();
    }
}