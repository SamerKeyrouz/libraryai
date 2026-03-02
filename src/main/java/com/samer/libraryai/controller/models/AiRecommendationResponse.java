package com.samer.libraryai.controller.models;

import lombok.Value;
import java.util.List;

@Value
public class AiRecommendationResponse {
    List<Recommendation> recommendations;

    @Value
    public static class Recommendation {
        String title;
        String author;
        String reason;
    }
}