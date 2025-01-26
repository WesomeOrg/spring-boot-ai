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
                You will be provided with text delimited by triple backticks.
                If it contains a sequence of instructions, re-write those instructions in the following format:

                Step 1 - ...
                Step 2 - ...

                ...
                Step N - ...

                If the text does not contain a sequence of instructions, then simply write "No steps provided”.
                ```{userMessageText}```
                """;

        var userMessageText = """
                Making a cup of tea is easy! First, you need to get some water boiling. While that's happening, grab a cup and put a tea bag in it. Once the water is hot enough, just pour it over the tea bag. Let it sit for a bit so the tea can steep. After a few minutes, take out the tea bag. If you like, you can add some sugar or milk to taste. And that's it! You've got yourself a delicious cup of tea to enjoy.""";

        var systemMessage = new SystemMessage(systemMessageText);
        var userMessage = new UserMessage(userMessageText);
        var prompt = new Prompt(List.of(systemMessage, userMessage));
        return chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getText();
    }
}