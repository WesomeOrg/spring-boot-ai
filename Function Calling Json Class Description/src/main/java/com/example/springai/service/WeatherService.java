package com.example.springai.service;

import com.example.springai.entity.Request;
import com.example.springai.entity.Response;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Configuration
public class WeatherService implements Function<Request, Response> {

    @Value("classpath:weather.json")
    Resource resourceFile;
    private @Autowired Gson gson;

    @Override
    public Response apply(Request request) {
        System.out.println("WeatherService.apply request = " + request);
        try {
            return gson.fromJson(resourceFile.getContentAsString(StandardCharsets.UTF_8), Response.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read weather json file " + e);
        }
    }
}
