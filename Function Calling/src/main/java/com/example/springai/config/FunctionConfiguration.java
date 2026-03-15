package com.example.springai.config;

import com.example.springai.service.WeatherService;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class FunctionConfiguration {

    @Bean
    public ToolCallback weatherFunctionInfo() {
        return FunctionToolCallback.builder("currentWeatherFunction", new WeatherService()).description("Get the current weather condition for the given city").inputType(WeatherService.Request.class).build();
    }
}
