package com.example.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SpringAiController {
    @Autowired
    private ChatModel chatModel;

    @GetMapping("/listOutputConverter")
    List<String> listOutputConverter(@RequestParam(value = "countryNames", defaultValue = "India, Canada") String countryNames) {
        var template = """
                For these list of countries {countryNames}, return there capitals.
                """;
        List<String> countries = ChatClient.create(chatModel)
                .prompt()
                .user(u -> u.text(template).param("countryNames", countryNames))
                .call()
                .entity(new ListOutputConverter(new DefaultConversionService()));
        return countries;
    }
}
