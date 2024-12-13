package com.example.springai.controller;

import com.example.springai.controller.entity.Code;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/entity")
    Code entity() {
        String helloPrompt = """
                Generate a coding language and there detail.
                Your response should be in JSON format.
                Do not include any explanations, only provide a RFC8259 compliant JSON response following this format without deviation.
                Do not include markdown code blocks in your response.
                Remove the ```json markdown from the output.
                Remove the * from the output
                """;
        return chatClient.prompt().user(helloPrompt).call().entity(Code.class);
    }
}