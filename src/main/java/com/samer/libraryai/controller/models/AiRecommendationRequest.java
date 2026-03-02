package com.samer.libraryai.controller.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class AiRecommendationRequest {

    @NotBlank(message = "Query is required")
    String query;
}