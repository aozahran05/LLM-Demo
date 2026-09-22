package com.invoiceq.backend.controller;

import com.invoiceq.backend.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
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
        this.aiClient = RestClient.builder()
                .requestFactory(new SimpleClientHttpRequestFactory())
                .baseUrl(aiServiceUrl)
                .build();
        this.defaultCompanyName = defaultCompanyName;
    }

    @PostMapping
    public Map<String, String> chat(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal AuthenticatedUser user) {

        String message = request.getOrDefault("message", "");
        String companyName = resolveCompanyName(request, user);
        String sessionId = request.getOrDefault("session_id", "default");
        String username = resolveUsername(request, user);

        Map<String, Object> aiRequest = new LinkedHashMap<>();
        aiRequest.put("company_name", companyName);
        aiRequest.put("question", message);
        aiRequest.put("session_id", sessionId);
        aiRequest.put("username", username);

        try {
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
        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            String errorMsg = "AI Service Error: " + ex.getResponseBodyAsString();
            Map<String, String> result = new LinkedHashMap<>();
            result.put("answer", errorMsg);
            result.put("response", errorMsg);
            return result;
        } catch (Exception ex) {
            String errorMsg = "Error connecting to AI service: " + ex.getMessage();
            Map<String, String> result = new LinkedHashMap<>();
            result.put("answer", errorMsg);
            result.put("response", errorMsg);
            return result;
        }
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

    private String resolveUsername(Map<String, String> request, AuthenticatedUser user) {
        if (user != null && user.email() != null && !user.email().isBlank()) {
            return user.email();
        }

        String requested = request.get("username");
        if (requested != null && !requested.isBlank()) {
            return requested;
        }

        return "guest";
    }
}