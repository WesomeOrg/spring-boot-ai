package com.example.springai.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.RelevancyEvaluator;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SpringAiControllerTest {
    @Autowired
    VectorStore vectorStore;
    @Autowired
    SpringAiController springAiController;
    @Autowired
    private ChatModel chatModel;
    private ChatClient chatClient;

    @BeforeEach
    public void setup() {
        chatClient = ChatClient.builder(chatModel).build();
    }

    @Test
    void simpleVectorStore() {
        var userText = "What is Spring Framework?";
        var response = springAiController.redisVectorStore(userText);
        var content = response.getResult().getOutput().getText();
        var relevancyEvaluator = new RelevancyEvaluator(ChatClient.builder(chatModel));
        var evaluationRequest = new EvaluationRequest(userText, response.getMetadata()
                .get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS), content);
        var evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
        assertTrue(evaluationResponse.isPass(), "Response is not relevant to the question");
    }
}