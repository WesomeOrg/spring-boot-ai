package com.example.springai.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.RelevancyEvaluator;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SpringAiControllerTest {
    @Autowired
    private VectorStore simpleVectorStore;

    @Autowired
    private ChatModel chatModel;
    private ChatClient chatClient;


    @BeforeEach
    public void setup() {
        chatClient = ChatClient.builder(chatModel).build();
    }

    @Test
    void falseFactCheckingEvaluator() {
        String userText = "The Macintosh is an operating system designed by Apple Inc";
        ChatResponse response = ChatClient.builder(chatModel)
                .build()
                .prompt()
                .advisors(new QuestionAnswerAdvisor(simpleVectorStore))
                .user(userText)
                .call()
                .chatResponse();
        String responseContent = response.getResult().getOutput().getText();
        var relevancyEvaluator = new RelevancyEvaluator(ChatClient.builder(chatModel));
        EvaluationRequest evaluationRequest = new EvaluationRequest(userText, response.getMetadata()
                .get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS), responseContent);
        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
        assertFalse(evaluationResponse.isPass(), "Response is not relevant to the question");
    }

    @Test
    void trueFactCheckingEvaluator() {
        String userText = "The Macintosh is an Apple";
        ChatResponse response = ChatClient.builder(chatModel)
                .build()
                .prompt()
                .advisors(new QuestionAnswerAdvisor(simpleVectorStore))
                .user(userText)
                .call()
                .chatResponse();
        String responseContent = response.getResult().getOutput().getText();
        var relevancyEvaluator = new RelevancyEvaluator(ChatClient.builder(chatModel));
        EvaluationRequest evaluationRequest = new EvaluationRequest(userText, response.getMetadata()
                .get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS), responseContent);
        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
        assertTrue(evaluationResponse.isPass(), "The claim is supported by the context");
    }
}