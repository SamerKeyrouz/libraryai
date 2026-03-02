package com.samer.libraryai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiClient {

    private final WebClient webClient;
    private final String apiKey;
    private final String model;

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper
            = new com.fasterxml.jackson.databind.ObjectMapper();

    public GeminiClient(
            WebClient.Builder builder,
            @Value("${gemini.api.key}") String apiKey,
            @Value("${gemini.model}") String model
    ) {
        this.webClient = builder
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
        this.apiKey = apiKey;
        this.model = model;
    }

    public String generateText(String prompt) {

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        try {
            String response = webClient.post()
                    .uri("/v1beta/models/{model}:generateContent?key={key}", model, apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // 🔥 Parse full Gemini response
            var root = objectMapper.readTree(response);

            String text = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            // 🔥 Remove markdown wrapping if exists
            text = text.replace("```json", "")
                    .replace("```", "")
                    .trim();

            return text;

        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
            System.out.println("Gemini status: " + e.getStatusCode());
            System.out.println("Gemini body: " + e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract AI response", e);
        }
    }
}