package com.example.springai.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpringAiController {
    private final Resource apples;
    @Autowired
    private ChatModel chatModel;

    public SpringAiController(@Value("classpath:apples.st") Resource apples) {
        this.apples = apples;
    }

    @GetMapping("/textDocument")
    List<Document> textDocument() {
        var textReader = new TextReader(apples);
        var documents = textReader.get();
        var splitter = new TokenTextSplitter(true);
        return splitCustomized(splitter.apply(documents));
    }

    public List<Document> splitCustomized(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter(100, 40, 10, 500, true);
        return splitter.apply(documents);
    }
}