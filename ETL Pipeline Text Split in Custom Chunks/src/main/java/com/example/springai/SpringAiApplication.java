package com.example.springai;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringAiApplication {
    @Autowired
    private ChatModel chatModel;

    public static void main(String[] args) {
        SpringApplication.run(SpringAiApplication.class, args);
    }
}
