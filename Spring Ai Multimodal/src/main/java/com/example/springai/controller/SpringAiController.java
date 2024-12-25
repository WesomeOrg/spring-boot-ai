package com.example.springai.controller;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class SpringAiController {
    @Autowired
    private ChatModel chatModel;

    @GetMapping("/hello")
    String hello() {
        var imageData = new ClassPathResource("/hello.png");
        var userMessage = new UserMessage("Explain what do you see in this picture?", List.of(new Media(MimeTypeUtils.IMAGE_PNG, imageData)));
        var response = this.chatModel.call(new Prompt(userMessage));
        return response.getResult().getOutput().getContent();
    }
}