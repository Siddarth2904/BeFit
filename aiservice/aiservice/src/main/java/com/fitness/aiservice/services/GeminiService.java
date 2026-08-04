package com.fitness.aiservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiUrl;
    @Value("${gemini.api.key}")
    private String geminiKey;

    public GeminiService(WebClient webClient) {
        this.webClient = webClient;
    }
    public String getAnswer(String question) {
        Map<String,Object> requestBody=Map.of(
                "contents",new Object[]{
                        Map.of("parts",new Object[]{
                                Map.of("text",question)
                        })
                }
        );
        String response=webClient.post().uri(geminiUrl+geminiKey)
                .header("Content-Type","application/json")
                .bodyValue(requestBody).retrieve()
                .bodyToMono(String.class).block();
        return response;
    }
}
