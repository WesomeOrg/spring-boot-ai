package com.example.springai.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SpringAiController {
    @Autowired
    private ChatModel chatModel;

    @GetMapping("/listOutputConverter")
    List<String> listOutputConverter(@RequestParam(value = "countryNames", defaultValue = "India, Canada") String countryNames) {
        var template = """
                For these list of countries {countryNames}, return there capitals.
                 {format}
                """;
        var listOutputConverter = new ListOutputConverter(new DefaultConversionService());
        var prompt = new PromptTemplate(template, Map.of("countryNames", countryNames, "format", listOutputConverter.getFormat())).create();
        var generation = this.chatModel.call(prompt).getResult();
        return listOutputConverter.convert(generation.getOutput().getText());
    }
}
