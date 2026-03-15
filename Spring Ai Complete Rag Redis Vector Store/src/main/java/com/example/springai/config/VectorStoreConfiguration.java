package com.example.springai.config;


import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import redis.clients.jedis.JedisPooled;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class VectorStoreConfiguration {
    @Value("classpath:/docs/menu.json")
    private Resource pdfResource;
    @Value("${spring.ai.vectorstore.redis.index}")
    private String redisIndex;
    @Value("${spring.ai.vectorstore.redis.prefix}")
    private String redisPrefix;
    @Value("${spring.ai.vectorstore.redis.initialize-schema}")
    private boolean redisInitializeSchema;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Bean
    RedisVectorStore redisVectorStore() {
        System.out.println("SpringAiApplication.redisVectorStore");
        RedisVectorStore vectorStore = RedisVectorStore.builder(new JedisPooled(), embeddingModel).indexName(redisIndex).prefix(redisPrefix).initializeSchema(redisInitializeSchema).vectorAlgorithm(RedisVectorStore.Algorithm.HNSW).build();


        List<String> documentIds = new ArrayList<>();
        List<Document> documentList = new ArrayList<>();
        JsonReader jsonReader = new JsonReader(pdfResource);
        List<Document> jsonDocuments = jsonReader.get();
        for (Document document : jsonDocuments) {
            documentIds.add(document.getId());
            document.getMetadata().put("file", "menu_json"); // this will add a metadata in each embedding, so when filter is applied, or search is happening , it will only search with this metadata, not working because index is not creating in redis
            documentList.add(document);
        }

//        String docId = "my-unique-id";
//        Document doc = new Document(docId, "Content", Map.of("country", "Netherlands"));
//        vectorStore.add(List.of(doc));
//        vectorStore.delete(List.of(docId));

// 2. Use that same ID to delete
        vectorStore.delete(documentIds);


        JedisPooled jedis = vectorStore.getJedis();
//         To delete all keys in *all* databases (use with extreme caution)
//        String flushedAll = jedis.flushAll();
//        System.out.println("redis FLUSHALL Response: " + flushedAll);

        var tokenTextSplitter = TokenTextSplitter.builder().build();
//        List<Document> documents = tokenTextSplitter.apply(documentList);
//        vectorStore.add(documents);
        return vectorStore;
    }
}