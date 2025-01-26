package com.example.springai.controller;

import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringAiController {
    @Autowired
    private VectorStore redisVectorStore;

    @GetMapping("/similaritySearch")
    public String similaritySearch(@RequestParam(value = "question", defaultValue = "What is Spring Framework?") String question) {
        return redisVectorStore.similaritySearch(SearchRequest.builder().query(question).build()).getFirst().getText();
    }
}