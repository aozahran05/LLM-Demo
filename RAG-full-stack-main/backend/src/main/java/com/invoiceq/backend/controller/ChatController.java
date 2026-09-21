package com.invoiceq.backend.controller;

import com.invoiceq.backend.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.LinkedHashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final RestClient aiClient;
    private final String defaultCompanyName;

    public ChatController(
            @Value("${ai.service.url}") String aiServiceUrl,
            @Value("${ai.default-company-name}") String defaultCompanyName) {
        this.aiClient = RestClient.builder().baseUrl(aiServiceUrl).build();
        this.defaultCompanyName = defaultCompanyName;
    }

    @PostMapping
    public Map<String, String> chat(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal AuthenticatedUser user) {

        String message = request.getOrDefault("message", "");
        String companyName = resolveCompanyName(request, user);

        Map<String, Object> aiRequest = Map.of(
                "company_name", companyName,
                "question", message
        );

        Map<String, Object> aiResponse = aiClient.post()
                .uri("/api/ai/ask")
                .contentType(MediaType.APPLICATION_JSON)
                .body(aiRequest)
                .retrieve()
                .body(Map.class);

        String answer = aiResponse == null
                ? "No response from AI service."
                : String.valueOf(aiResponse.getOrDefault("answer", ""));

        Map<String, String> result = new LinkedHashMap<>();
        result.put("answer", answer);
        result.put("response", answer);
        return result;
    }

    private String resolveCompanyName(Map<String, String> request, AuthenticatedUser user) {
        if (user != null && user.companyName() != null && !user.companyName().isBlank()) {
            return user.companyName();
        }

        String requested = request.get("company_name");
        if (requested != null && !requested.isBlank()) {
            return requested;
        }

        return defaultCompanyName;
    }
}