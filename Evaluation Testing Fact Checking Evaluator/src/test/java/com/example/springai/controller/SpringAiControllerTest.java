package com.example.springai.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.FactCheckingEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class SpringAiControllerTest {
    @Autowired
    private ChatModel chatModel;
    private ChatClient chatClient;

    @Value ("classpath:/documents/apples.st")
    private Resource appleSt;

    @BeforeEach
    public void setup() {
        chatClient = ChatClient.builder(chatModel).build();
    }

    @Test
    void factCheckingEvaluator() throws IOException {
        var factCheckingEvaluator = new FactCheckingEvaluator(ChatClient.builder(chatModel));
        String claim = "The Macintosh is an operating system designed by Apple Inc";
        EvaluationRequest evaluationRequest = new EvaluationRequest(appleSt.getContentAsString(StandardCharsets.UTF_8), Collections.emptyList(), claim);
        EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);
        assertFalse(evaluationResponse.isPass(), "The claim should not be supported by the context");
    }
}