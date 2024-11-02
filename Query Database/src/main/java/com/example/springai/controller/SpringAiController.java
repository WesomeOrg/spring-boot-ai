package com.example.springai.controller;

import com.example.springai.entity.AppleDbAiResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.Charset;

@RestController
public class SpringAiController {
    private final ChatClient chatClient;
    @Value("classpath:/apple_schema.sql")
    private Resource appleSchema;
    @Value("classpath:/apple_ai_prompt_template.st")
    private Resource appleAiPromptTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping(path = "/query")
    public AppleDbAiResponse query(@RequestParam(value = "question") String question) throws IOException {
        String appleDataBaseSchema = appleSchema.getContentAsString(Charset.defaultCharset());
        String query = chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .user(userSpec -> userSpec.text(appleAiPromptTemplate)
                        .param("question", question)
                        .param("ddl", appleDataBaseSchema))
                .call()
                .content();
        return new AppleDbAiResponse(query, jdbcTemplate.queryForList(query));
    }
}