package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAiController {

    @GetMapping("/hello")
    String hello() {
        ChatModel myChatModel = new OllamaChatModel(new OllamaApi());
        ChatClient chatClient = ChatClient.create(myChatModel);
        String helloPrompt = "Hello, I am learning Ai with Spring";
        return chatClient.prompt().user(helloPrompt).call().content();
    }

    @GetMapping("/helloWithDefaultModel")
    String helloWithDefaultModel() {
        ChatModel myChatModel = new OllamaChatModel(new OllamaApi(), OllamaOptions.create()
                .withModel(OllamaOptions.DEFAULT_MODEL));
        ChatClient chatClient = ChatClient.create(myChatModel);
        String helloPrompt = "Hello, I am learning Ai with Spring";
        return chatClient.prompt().user(helloPrompt).call().content();
    }
}