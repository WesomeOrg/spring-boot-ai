package com.example.springai;

import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class SpringAiApplication {
    @Value("${documentDirectory}")
    private String documentDirectory;

    @Autowired
    private VectorStore vectorStore;
    @Value("${inputFilenamePattern}")
    private String inputFilenamePattern;

    public static void main(String[] args) {
        SpringApplication.run(SpringAiApplication.class, args);
    }

    @Bean
    VectorStore redisVectorStore() {
        var tokenTextSplitter = new TokenTextSplitter();
        System.out.println("SpringAiApplication.redisVectorStore");
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(Path.of(documentDirectory), inputFilenamePattern)) {
            for (Path path : paths) {
                TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(new ByteArrayResource(Files.readAllBytes(path)));
                this.vectorStore.accept(tokenTextSplitter.apply(tikaDocumentReader.get()));
            }
        } catch (IOException e) {
            System.out.println("e = " + e);
        }
        return vectorStore;
    }
}
