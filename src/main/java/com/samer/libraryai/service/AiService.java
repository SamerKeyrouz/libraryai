package com.samer.libraryai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samer.libraryai.controller.models.AiRecommendationResponse;
import com.samer.libraryai.entity.Book;
import com.samer.libraryai.repository.BookRepository;
import com.samer.libraryai.service.GeminiClient;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AiService {

    private final GeminiClient geminiClient;
    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper;

    public AiRecommendationResponse recommendBooks(String query, String email) {

        List<Book> books = bookRepository
                .findAll(PageRequest.of(0, 20))
                .getContent();

        StringBuilder context = new StringBuilder();
        context.append("Available books:\n");

        for (Book book : books) {
            context.append("- ")
                    .append(book.getTitle())
                    .append(" by ")
                    .append(book.getAuthor())
                    .append("\n");
        }

        System.out.println("===== BOOK CONTEXT SENT TO AI =====");
        System.out.println(context.toString());
        System.out.println("===================================");

        String prompt = """
        You are a smart librarian AI.
        
        The user email is: %s
        
        User wants:
        "%s"
        
        Based on the available books below:
        - Only recommend books that STRONGLY match the user's request.
        - A strong match means the main theme of the book clearly aligns with the user's query.
        - Do NOT stretch connections.
        - If a book only loosely related, DO NOT include it.
        - If only 1 or 2 books strongly match, return only those.
        - If none strongly match, return an empty recommendations array.
        
        Match primarily by:
        - Core theme
        - Genre
        - Explicit topic mentioned in the query.
        
        
        Return STRICT JSON:
        {
          "recommendations": [
            {
              "title": "string",
              "author": "string",
              "reason": "string"
            }
          ]
        }
        
        Available books:
        %s
        """.formatted(email, query, context.toString());

        String rawResponse = geminiClient.generateText(prompt);

        System.out.println("===== RAW AI RESPONSE =====");
        System.out.println(rawResponse);
        System.out.println("===========================");

        try {
            return objectMapper.readValue(rawResponse, AiRecommendationResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }
}