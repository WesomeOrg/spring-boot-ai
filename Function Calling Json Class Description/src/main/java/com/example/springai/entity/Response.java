package com.example.springai.entity;

public record Response(Location location, Current current) {
}

record Current(int last_updated_epoch, String last_updated, int temp_c, double temp_f, int is_day, Condition condition,
               double wind_mph, double wind_kph, int wind_degree, String wind_dir, int pressure_mb, double pressure_in,
               double precip_mm, int precip_in, int humidity, int cloud, double feelslike_c, double feelslike_f,
               int vis_km, int vis_miles, int uv, double gust_mph, double gust_kph, AirQuality air_quality) {
}

record Location(String name, String region, String country, double lat, double lon, String tz_id, int localtime_epoch,
                String localtime) {
}

record AirQuality(double co, double no2, double o3, double so2, double pm2_5, double pm10) {
}

record Condition(String text, String icon, int code) {
}