package com.example.springai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem(s -> s.text("You are a helpful assistant").metadata("assistantType", "general").metadata("version", "1.0")).defaultUser(u -> u.text("Default user context").metadata("sessionId", "default-session")).build();
    }
}