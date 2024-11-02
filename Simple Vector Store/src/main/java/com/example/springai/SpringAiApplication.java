package com.example.springai;

import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class SpringAiApplication {

    @Value("vectorStore.json")
    private String vectorStoreFileName;

    @Value("classpath:/faq/spring-faq.txt")
    private Resource springFaq;

    public static void main(String[] args) {
        SpringApplication.run(SpringAiApplication.class, args);
    }

    @Bean
    SimpleVectorStore simpleVectorStore() {
        System.out.println("SpringAiApplication.simpleVectorStore");
        OllamaEmbeddingModel ollamaEmbeddingModel = new OllamaEmbeddingModel(new OllamaApi());
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(ollamaEmbeddingModel);
        File vectorStoreFile = getVectorStoreFile();
        if (vectorStoreFile.exists() && vectorStoreFile.length() != 0) {
            simpleVectorStore.load(vectorStoreFile);
        } else {
            TextReader textReader = new TextReader(springFaq);
            textReader.getCustomMetadata().put("filename", "spring-faq.txt");
            List<Document> documents = textReader.get();
            TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = tokenTextSplitter.apply(documents);
            simpleVectorStore.add(splitDocuments);
            simpleVectorStore.save(vectorStoreFile);
        }
        return simpleVectorStore;
    }


    private File getVectorStoreFile() {
        Path path = Paths.get("src/main/resources/data");
        var vectorStoreFile = path.toFile().getAbsolutePath() + "\\" + vectorStoreFileName;
        return new File(vectorStoreFile);
    }

}
