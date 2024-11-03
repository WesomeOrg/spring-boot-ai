package com.example.springai.controller;

import com.example.springai.entity.Country;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SpringAiController {
    @Autowired
    private ChatModel chatModel;

    @GetMapping("/beanOutputParser")
    Country hello(@RequestParam(value = "letter", defaultValue = "a") String letter) {
        var template = """
                Give me a country starts with {letter} and its capital.
                {format}
                """;
        var outputConverter = new BeanOutputConverter<Country>(new ParameterizedTypeReference<>() {
        });
        var prompt = new PromptTemplate(template, Map.of("letter", letter, "format", outputConverter.getFormat())).create();
        var generation = chatModel.call(prompt).getResult();
        return outputConverter.convert(generation.getOutput().getContent());
    }
}
