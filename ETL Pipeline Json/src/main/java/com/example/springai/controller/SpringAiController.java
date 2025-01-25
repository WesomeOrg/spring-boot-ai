package com.example.springai.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpringAiController {
    private final Resource apples;

    public SpringAiController(@Value("classpath:apples.json") Resource apples) {
        this.apples = apples;
    }

    @GetMapping("/jsonDocument")
    List<Document> jsonDocument() {
        JsonReader jsonReader = new JsonReader(this.apples, "appleName", "taste");
        return jsonReader.get();
    }
}