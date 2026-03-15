package com.example.springai.controller.tool;


import com.example.springai.config.SearchEngineDocumentRetriever;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import static java.util.Locale.ENGLISH;

@Component
public class Tools {
    private static final Logger logger = LoggerFactory.getLogger(Tools.class);
    private final ChatClient.Builder chatClientBuilder;
    private final RestClient.Builder restClientBuilder;
    private final VectorStore vectorStore;

    Tools(ChatClient.Builder chatClientBuilder, RestClient.Builder restClientBuilder, VectorStore vectorStore) {
        this.chatClientBuilder = chatClientBuilder;
        this.restClientBuilder = restClientBuilder;
        this.vectorStore = vectorStore;
    }

    @Tool(description = "Retrieve information about coffee shop menu")
    @Nullable String menuJsonRetriever(String query) {
        logger.debug("Tools.menuJsonRetriever query : {} ", query);
        String response = chatClientBuilder.clone()
                .build()
                .prompt()
                .advisors(RetrievalAugmentationAdvisor.builder()
                        .documentRetriever(VectorStoreDocumentRetriever.builder()
//                                .filterExpression(new FilterExpressionBuilder()
//                                .eq("file", "menu_json")
//                                .build())                                /*  will get results from metadata which has below keys only, it will only search with this metadata, not working because index is not creating in redis */
                .vectorStore(vectorStore)
                                .similarityThreshold(0.5)
                                .topK(3)
                                .build())
                        .build())
                .user(query)
                .call()
                .content();
        logger.debug("Tools.menuJsonRetriever response : {} ", response);
        return response;
    }

    @Tool(description = "Retrieve information about another meta data")  // update this description
    @Nullable String storyRetriever(String query) {
        logger.debug("Tools.storyRetriever query : {} ", query);
        String response = chatClientBuilder.clone()
                .build()
                .prompt()
                .advisors(RetrievalAugmentationAdvisor.builder()
                        .documentRetriever(VectorStoreDocumentRetriever.builder()
                                .filterExpression(new FilterExpressionBuilder()
                                        .eq("topic", "story")
                                        .build())    /*  will get results from metadata which has below keys only, it will only search with this metadata, not working because index is not creating in redis */.vectorStore(vectorStore)
                                .similarityThreshold(0.5)
                                .topK(3)
                                .build())
                        .build())
                .user(query)
                .call()
                .content();
        logger.debug("Tools.storyRetriever response : {} ", response);
        return response;
    }

    @Tool(description = "Retrieve information by searching the web")
    @Nullable String webSearchRetriever(String query) {
        return chatClientBuilder.clone()
                .build()
                .prompt()
                .advisors(RetrievalAugmentationAdvisor.builder()
                        .documentRetriever(SearchEngineDocumentRetriever.builder()
                                .restClientBuilder(restClientBuilder)
                                .maxResults(10)
                                .build())
                        .build())
                .user(query)
                .call()
                .content();
    }

    @Tool(description = "Retrieve information by first translating query from another language to English")
    @Nullable String languageTranslateAdvisor(String query) {
        logger.debug("Tools.languageTranslateAdvisor query : {} ", query);
        String languageTranslate = chatClientBuilder.clone()
                .build()
                .prompt()
                .advisors(RetrievalAugmentationAdvisor.builder()
                        .queryTransformers(TranslationQueryTransformer.builder()
                                .targetLanguage(ENGLISH.getLanguage())
                                .build(), RewriteQueryTransformer.builder()
                                .targetSearchSystem("vector store")
                                .build())
                        .documentRetriever(VectorStoreDocumentRetriever.builder()
                                .vectorStore(vectorStore)
                                .similarityThreshold(0.5)
                                .topK(3)
                                .build())
                        .build())
                .user(query)
                .call()
                .content();
        logger.debug("Tools.menuJsonRetriever languageTranslate : {} ", languageTranslate);
        return languageTranslate;
    }
}