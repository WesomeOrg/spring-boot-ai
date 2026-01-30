package com.example.springai.entity;

import java.util.List;
import java.util.Map;

public record AppleDbAiResponse(String sqlQuery, List<Map<String, Object>> results) {
}
