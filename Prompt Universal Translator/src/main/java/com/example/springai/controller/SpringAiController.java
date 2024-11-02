package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/prompt")
    public Stream<String> prompt() {
        List<String> language = List.of("La performance du système est plus lente que d'habitude.",
                "Mi monitor tiene píxeles que no se iluminan.",
                "Il mio mouse non funziona",
                "Mój klawisz Ctrl jest zepsuty",
                "我的屏幕在闪烁");
        String systemMessageText = """
                Translate the following text to English and Korean:
                ```{userMessage}```
                """;
        var systemMessage = new SystemMessage(systemMessageText);
        return language.stream()
                .map(UserMessage::new)
                .map(userMessage -> new Prompt(List.of(systemMessage, userMessage)))
                .map(prompt1 -> chatClient.prompt(prompt1).call().chatResponse().getResult().getOutput().getContent());

    }
}