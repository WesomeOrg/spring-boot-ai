package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        List<String> forbiddenProgrammingLanguages = List.of("C++", "Python", "C#", "Ruby", "JavaScript", "PHP", "Perl", "Scala", "Kotlin");
        SafeGuardAdvisor safeGuardAdvisor = new SafeGuardAdvisor(forbiddenProgrammingLanguages,"sorry we learn Java only.", 1);
        this.chatClient = chatClient.defaultAdvisors(safeGuardAdvisor).build();
    }

    @GetMapping("/hello")
    String hello(@RequestParam(value = "prompt", defaultValue = "Hello, I am learning Ai with Spring") String prompt) {
        return this.chatClient.prompt().user(prompt).call().content();
    }
}