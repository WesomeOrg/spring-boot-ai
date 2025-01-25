package com.example.springai.controller;

import com.example.springai.entity.Country;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SpringAiController {
    @Autowired
    private ChatModel chatModel;


    @GetMapping("/beanOutputParser")
    Country beanOutputParser() {
        var prompt = """
                Give me a country and its capital.
                  {format}
                """;
        var beanOutputConverter = new BeanOutputConverter<>(Country.class);
        var generation = chatModel.call(new PromptTemplate(prompt, Map.of("format", beanOutputConverter.getFormat())).create())
                .getResult();
        return beanOutputConverter.convert(generation.getOutput().getText());
    }
}
