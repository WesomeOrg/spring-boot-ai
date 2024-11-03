package com.example.springai.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Configuration
public class WeatherService implements Function<WeatherService.Request, WeatherService.Response> {

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

    public record Request(String city) {
    }

    record AirQuality(double co, double no2, double o3, double so2, double pm2_5, double pm10) {
    }

    record Condition(String text, String icon, int code) {
    }

    record Current(int last_updated_epoch, String last_updated, int temp_c, double temp_f, int is_day,
                   Condition condition, double wind_mph, double wind_kph, int wind_degree, String wind_dir,
                   int pressure_mb, double pressure_in, double precip_mm, int precip_in, int humidity, int cloud,
                   double feelslike_c, double feelslike_f, int vis_km, int vis_miles, int uv, double gust_mph,
                   double gust_kph, AirQuality air_quality) {
    }

    record Location(String name, String region, String country, double lat, double lon, String tz_id,
                    int localtime_epoch, String localtime) {
    }

    public record Response(Location location, Current current) {
    }
}
