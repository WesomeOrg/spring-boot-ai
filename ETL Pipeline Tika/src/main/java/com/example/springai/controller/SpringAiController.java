package com.example.springai.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SpringAiController {
    @Value("${documentDirectory}")
    private Resource documentDirectory;

    @Value("${inputFilenamePattern}")
    private String inputFilenamePattern;

    @GetMapping("/pdfDocument")
    List<Document> pdfDocument() throws IOException {
        List<Document> documentList = new ArrayList<>();
        for (Path path : Files.newDirectoryStream(Path.of(documentDirectory.getURI()), inputFilenamePattern))
            new TikaDocumentReader(new ByteArrayResource(Files.readAllBytes(path))).get().forEach(document -> {
                document.getMetadata().put("source", path.getFileName());
                documentList.add(document);
            });
        return documentList;
    }
}