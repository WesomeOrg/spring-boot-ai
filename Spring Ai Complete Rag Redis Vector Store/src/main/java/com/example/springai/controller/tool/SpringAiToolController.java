package com.example.springai.controller.tool;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tool")
public class SpringAiToolController {
    private final String CONVERSATION_ID = UUID.randomUUID().toString();
    private final ChatClient chatClient;
    private final RedisVectorStore redisVectorStore;

    public SpringAiToolController(ChatClient.Builder builder, RedisVectorStore redisVectorStore, Tools tools) {
        this.redisVectorStore = redisVectorStore;
        this.chatClient = builder.defaultTools(tools).build();
    }

    @GetMapping("/redisVectorStore")
    public String redisVectorStore(@RequestParam(value = "question", defaultValue = "What is Spring Framework?") String question) {
        ChatResponse response = chatClient.prompt().advisors(a -> a.param(ChatMemory.CONVERSATION_ID, CONVERSATION_ID)).user(question).call().chatResponse();
        return response.getResult().getOutput().getText();
    }

    @GetMapping("/redisVectorStoreSimilaritySearch")
    public List<Document> redisVectorStoreSimilaritySearch(@RequestParam(value = "question", defaultValue = "What is Spring Framework?") String question) {
        List<Document> results = redisVectorStore.similaritySearch(SearchRequest.builder().query(question).topK(5).similarityThresholdAll().build());
        return results;
    }
}
