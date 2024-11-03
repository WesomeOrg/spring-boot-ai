package com.example.springai.entity;

import com.fasterxml.jackson.annotation.JsonClassDescription;

@JsonClassDescription("Get the current weather condition for the given city")
public record Request(String city) {
}