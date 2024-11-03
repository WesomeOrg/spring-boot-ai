package com.example.springai.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@RestController
public class SpringAiController {
    private final Resource apples;

    public SpringAiController(@Value("classpath:apples.st") Resource apples) {
        this.apples = apples;
    }

    @GetMapping("/textDocument")
    List<Document> textDocument() throws IOException {
        var textReader = new TextReader(apples);
        textReader.getCustomMetadata()
                .putAll(Map.of("length", apples.contentLength(), "last modified", LocalDateTime.ofInstant(Instant.ofEpochMilli(apples.lastModified()), ZoneId.systemDefault())));
        var documents = textReader.get();
        return new TokenTextSplitter(true).apply(documents);
    }

}