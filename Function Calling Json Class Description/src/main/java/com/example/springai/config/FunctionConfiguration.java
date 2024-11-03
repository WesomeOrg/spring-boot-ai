package com.example.springai.config;

import com.example.springai.entity.Request;
import com.example.springai.entity.Response;
import com.example.springai.service.WeatherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class FunctionConfiguration {
    @Bean
    public Function<Request, Response> currentWeatherFunction() {
        return new WeatherService();
    }
}
