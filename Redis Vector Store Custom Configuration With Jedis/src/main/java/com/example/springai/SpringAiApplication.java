package com.example.springai;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import redis.clients.jedis.JedisPooled;

@SpringBootApplication
public class SpringAiApplication {
    @Value("classpath:/docs/spring-faq.pdf")
    private Resource pdfResource;
    @Value("${spring.ai.vectorstore.redis.port}")
    private int redisPort;
    @Value("${spring.ai.vectorstore.redis.uri}")
    private String redisUri;
    @Value("${spring.ai.vectorstore.redis.index}")
    private String redisIndex;
    @Value("${spring.ai.vectorstore.redis.prefix}")
    private String redisPrefix;
    @Value("${spring.ai.vectorstore.redis.embeddingFieldName}")
    private String redisEmbeddingFieldName;
    @Value("${spring.ai.vectorstore.redis.contentFieldName}")
    private String redisContentFieldName;
    @Value("${spring.ai.vectorstore.redis.initialize-schema}")
    private boolean redisInitializeSchema;


    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private EmbeddingModel embeddingModel;

    public static void main(String[] args) {
        SpringApplication.run(SpringAiApplication.class, args);
    }

    @Bean
    VectorStore redisVectorStore() {
        System.out.println("SpringAiApplication.redisVectorStore");
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource, PdfDocumentReaderConfig.builder()
                .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                        .withNumberOfBottomTextLinesToDelete(3)
                        .withNumberOfTopPagesToSkipBeforeDelete(1)
                        .build())
                .withPagesPerDocument(1)
                .build());
        var tokenTextSplitter = new TokenTextSplitter();
        RedisVectorStore vectorStore = RedisVectorStore.builder(jedisPooled(), embeddingModel)
                .indexName(redisIndex)
                .prefix(redisPrefix)
                .initializeSchema(redisInitializeSchema)
                .vectorAlgorithm(RedisVectorStore.Algorithm.HSNW)
                .batchingStrategy(new TokenCountBatchingStrategy())
                .contentFieldName(redisContentFieldName)
                .metadataFields(RedisVectorStore.MetadataField.tag("spring"), RedisVectorStore.MetadataField.numeric("year"), RedisVectorStore.MetadataField.text("description"))
                .build();
        vectorStore.accept(tokenTextSplitter.apply(pdfReader.get()));
        return vectorStore;
    }

    @Bean
    public JedisPooled jedisPooled() {
        JedisPooled jedisPooled = new JedisPooled(redisUri, redisPort);
        return jedisPooled;
    }
}
