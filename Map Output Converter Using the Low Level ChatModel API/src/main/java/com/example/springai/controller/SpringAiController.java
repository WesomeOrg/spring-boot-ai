package com.example.springai.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SpringAiController {
    @Autowired
    private ChatModel chatModel;

    @GetMapping("/mapOutputParser")
    Map<String, Object> mapOutputParser(@RequestParam(value = "countryNames", defaultValue = "India, Canada") String countryNames) {
        var template = """
                For these list of countries {countryNames}, return there capitals.
                {format}
                """;
        var mapOutputConverter = new MapOutputConverter();
        var prompt = new PromptTemplate(template, Map.of("countryNames", countryNames, "format", mapOutputConverter.getFormat())).create();
        var generation = chatModel.call(prompt).getResult();
        return mapOutputConverter.convert(generation.getOutput().getContent());
    }
}
