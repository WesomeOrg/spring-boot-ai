package com.example.springai;

import org.springframework.ai.document.Document;
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
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringAiApplication {

    @Value("vectorStore.json")
    private String vectorStoreFileName;

    @Value("classpath:/docs/spring-faq.pdf")
    private Resource pdfResource;

    //    @Value("${documentDirectory}")
//    private Resource documentDirectory;
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
    VectorStore redisVectorStore() throws IOException {
        System.out.println("SpringAiApplication.redisVectorStore");
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(Path.of(documentDirectory), inputFilenamePattern)) {

//        paths.forEach(path -> {


//            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(path.toString(), PdfDocumentReaderConfig.builder()
//                    .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
//                            .withNumberOfBottomTextLinesToDelete(3)
//                            .withNumberOfTopPagesToSkipBeforeDelete(1)
//                            .build())
//                    .withPagesPerDocument(1)
//                    .build());
//            var tokenTextSplitter = new TokenTextSplitter();
////        this.vectorStore.accept(tokenTextSplitter.apply(documentList));
//            this.vectorStore.accept(tokenTextSplitter.apply(pdfReader.get()));
//        });

            List<Document> documentList = new ArrayList<>();
            for (Path path : Files.newDirectoryStream(Path.of(documentDirectory), inputFilenamePattern)) {
                new TikaDocumentReader(new ByteArrayResource(Files.readAllBytes(path))).get().forEach(document -> {
                    System.out.println("document = " + document.getContent());
                    document.getMetadata().put("source", path.getFileName());
                    documentList.add(document);
                });
            }

            var tokenTextSplitter = new TokenTextSplitter();
            this.vectorStore.accept(tokenTextSplitter.apply(documentList));
//        this.vectorStore.accept(tokenTextSplitter.apply(pdfReader.get()));
        } catch (IOException e) {
            System.out.println("e = " + e);
        }


        return vectorStore;
    }
}
