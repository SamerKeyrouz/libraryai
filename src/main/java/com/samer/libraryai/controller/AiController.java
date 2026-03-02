package com.samer.libraryai.controller;

import com.samer.libraryai.controller.models.AiRecommendationRequest;
import com.samer.libraryai.controller.models.AiRecommendationResponse;
import com.samer.libraryai.service.AiService;
import com.samer.libraryai.service.GeminiClient;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;


    @PostMapping("/recommend")
    public AiRecommendationResponse recommend(
            @RequestBody AiRecommendationRequest request,
            @AuthenticationPrincipal OAuth2User principal
    ) {

        String email = principal.getAttribute("email");

        return aiService.recommendBooks(request.getQuery(), email);
    }


}