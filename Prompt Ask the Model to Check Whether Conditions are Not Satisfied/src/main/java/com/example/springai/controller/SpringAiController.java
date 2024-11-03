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
        String systemMessageText = """
                You will be provided with text delimited by triple backticks.
                If it contains a sequence of instructions, re-write those instructions in the following format:

                Step 1 - ...
                Step 2 - ...

                ...
                Step N - ...

                If the text does not contain a sequence of instructions, then simply write "No steps provided".
                ```{systemMessageText}```
                """;

        String userMessageText = "The sun is shining brightly today, and the birds are singing. It's a beautiful day to go for a walk in the park. The flowers are blooming, and the trees are swaying gently in the breeze. People are out and about, enjoying the lovely weather. Some are having picnics, while others are playing games or simply relaxing on the grass. It's a perfect day to spend time outdoors and appreciate the beauty of nature.";

        var systemMessage = new SystemMessage(systemMessageText);
        var userMessage = new UserMessage(userMessageText);
        var prompt = new Prompt(List.of(systemMessage, userMessage));
        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();
    }
}