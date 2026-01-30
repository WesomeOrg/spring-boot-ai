package com.example.springai.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"capital", "country"})
public record Country(String country, String capital) {
}