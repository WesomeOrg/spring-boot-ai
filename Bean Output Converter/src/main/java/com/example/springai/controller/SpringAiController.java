package com.example.springai.controller;

import com.example.springai.entity.Country;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.defaultAdvisors(new SimpleLoggerAdvisor()).build();
    }

    @GetMapping("/beanOutputParser")
    Country hello() {
        var prompt = """
                Give me a country and its capital.
                  {format}
                """;
        var beanOutputConverter = new BeanOutputConverter<Country>(Country.class);
        var generation = chatClient.prompt(new PromptTemplate(prompt, Map.of("format", beanOutputConverter.getFormat())).create())
                .call()
                .chatResponse()
                .getResult()
                .getOutput();
        return beanOutputConverter.convert(generation.getContent());
    }
}
