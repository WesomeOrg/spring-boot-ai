package com.example.springai.controller.advisor;

import com.example.springai.config.CompressionDocumentPostProcessor;
import com.example.springai.config.SearchEngineDocumentRetriever;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Locale.ENGLISH;

@RestController
@RequestMapping("/advisor")
public class SpringAiAdvisorController {
    private final String CONVERSATION_ID = UUID.randomUUID().toString();
    private final ChatClient chatClient;
    private final RedisVectorStore redisVectorStore;

    public SpringAiAdvisorController(ChatClient.Builder builder, RedisVectorStore redisVectorStore, RestClient.Builder restClientBuilder) {
        this.redisVectorStore = redisVectorStore;
        List<Advisor> advisors = new ArrayList<>();
        /*  convert context into question answer format */
        var questionAnswerAdvisor = QuestionAnswerAdvisor.builder(redisVectorStore).searchRequest(SearchRequest.builder().similarityThreshold(0.5).topK(10).build()).build();

        /*  get the information from vector store   */
        var vectorStoreDocumentRetriever = RetrievalAugmentationAdvisor.builder().documentRetriever(VectorStoreDocumentRetriever.builder().vectorStore(redisVectorStore).similarityThreshold(0.5).topK(10).build()).documentPostProcessors(CompressionDocumentPostProcessor.builder().chatClientBuilder(builder.clone()).build()).build();

        /*  search the https://app.tavily.com and get the result    */
        var searchEngineDocumentRetriever = RetrievalAugmentationAdvisor.builder().documentRetriever(SearchEngineDocumentRetriever.builder().restClientBuilder(restClientBuilder).maxResults(10).build()).documentPostProcessors(CompressionDocumentPostProcessor.builder().chatClientBuilder(builder.clone()).build()).build();

        /* ask question in different language , but model will understand in English */
        var translationQueryTransformer = RetrievalAugmentationAdvisor.builder().queryTransformers(TranslationQueryTransformer.builder().chatClientBuilder(builder.clone()).targetLanguage(ENGLISH.getLanguage()).build(), RewriteQueryTransformer.builder().chatClientBuilder(builder.clone()).targetSearchSystem("vector store").build()).documentRetriever(VectorStoreDocumentRetriever.builder().vectorStore(redisVectorStore).similarityThreshold(0.5).topK(3).build()).build();

        /* create multiple queries from same question to get all aspects of answer */
        var queryExpander = RetrievalAugmentationAdvisor.builder().queryExpander(MultiQueryExpander.builder().chatClientBuilder(builder.clone()).numberOfQueries(10).includeOriginal(true).build()).documentRetriever(VectorStoreDocumentRetriever.builder().vectorStore(redisVectorStore).similarityThreshold(0.5).topK(10).build()).build();

        /*  will get results from metadata which has below keys only, it will only search with this metadata, not working because index is not creating in redis */
        var filterExpressionDocumentRetriever = RetrievalAugmentationAdvisor.builder().documentRetriever(VectorStoreDocumentRetriever.builder().filterExpression(new FilterExpressionBuilder().eq("file", "menu_json").build()).vectorStore(redisVectorStore).similarityThreshold(0.5).topK(3).build()).build();

//        advisors.add(vectorStoreDocumentRetriever);
//        advisors.add(searchEngineDocumentRetriever);
//        advisors.add(questionAnswerAdvisor);
//        advisors.add(translationQueryTransformer);
        advisors.add(filterExpressionDocumentRetriever);
        advisors.add(queryExpander);
        this.chatClient = builder.defaultAdvisors(advisors).build();
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
