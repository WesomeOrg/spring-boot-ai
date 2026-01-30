package com.example.springai.controller;

import org.springframework.ai.document.DefaultContentFormatter;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.ContentFormatTransformer;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpringAiController {
    private final Resource apples;

    public SpringAiController(@Value("classpath:apples.st") Resource apples) {
        this.apples = apples;
    }

    @GetMapping("/textDocument")
    List<Document> textDocument() {
        var textReader = new TextReader(apples);
        var documents = textReader.get();
        var splitter = new TokenTextSplitter(true);
        return contentFormatTransformer(splitter.apply(documents));
    }

    List<Document> contentFormatTransformer(List<Document> documents) {
        var defaultContentFormatter = DefaultContentFormatter.defaultConfig();
        var contentFormatTransformer = new ContentFormatTransformer(defaultContentFormatter);
        return contentFormatTransformer.apply(documents);
    }
}