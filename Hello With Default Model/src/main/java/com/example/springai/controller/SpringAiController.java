package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAiController {

    @GetMapping("/hello")
    String hello() {
        var myChatModel = OllamaChatModel.builder()
                .ollamaApi(new OllamaApi())
                .defaultOptions(OllamaOptions.builder().model(OllamaModel.LLAMA3_1).build())
                .build();
        ChatClient chatClient = ChatClient.create(myChatModel);
        String helloPrompt = "Hello, I am learning Ai with Spring";
        return chatClient.prompt().user(helloPrompt).call().content();
    }

    @GetMapping("/helloWithDefaultModel")
    String helloWithDefaultModel() {
        var myChatModel = OllamaChatModel.builder()
                .ollamaApi(new OllamaApi())
                .defaultOptions(OllamaOptions.builder().model(OllamaModel.LLAMA3_1).build())
                .build();
        ChatClient chatClient = ChatClient.create(myChatModel);
        String helloPrompt = "Hello, I am learning Ai with Spring";
        return chatClient.prompt().user(helloPrompt).call().content();
    }
}