package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpringAiController {

    private final ChatClient chatClient;

    @Autowired
    private VectorStore redisVectorStore;

    public SpringAiController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/redisVectorStore")
    public String redisVectorStore(@RequestParam(value = "question", defaultValue = "What is Spring Framework?") String question) {
        ChatResponse response = chatClient.prompt()
                .advisors(new QuestionAnswerAdvisor(redisVectorStore))
                .user(question)
                .call()
                .chatResponse();
        return response.getResult().getOutput().getContent();
    }

    @GetMapping("/redisVectorStoreSimilaritySearch")
    public List<Document> redisVectorStoreSimilaritySearch(@RequestParam(value = "question", defaultValue = "What is Spring Framework?") String question) {
        List<Document> results = redisVectorStore.similaritySearch(SearchRequest.builder()
                .query(question)
                .topK(5)
                .similarityThresholdAll()
                .build());
        return results;
    }
}