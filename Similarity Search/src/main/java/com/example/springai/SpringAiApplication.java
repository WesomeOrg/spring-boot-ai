package com.example.springai;

import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class SpringAiApplication {

    @Value("vectorStore.json")
    private String vectorStoreFileName;

    @Value("classpath:/docs/spring-faq.pdf")
    private Resource pdfResource;

    @Autowired
    private VectorStore vectorStore;

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
        this.vectorStore.accept(tokenTextSplitter.apply(pdfReader.get()));
        return vectorStore;
    }
}
