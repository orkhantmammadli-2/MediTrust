package com.ltc.aiservice.service;

import com.ltc.aiservice.dto.GeminiResponse;
import com.ltc.aiservice.dto.InsightRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final WebClient webClient;

    @Value("${gemini.api-key}")
    private String apiKey;

    public String generateInsight(
            InsightRequest request
    ) {

        String prompt = """
Siz səhiyyə analitikası köməkçisisiniz.

Aşağıdakı statistikaya əsasən,
qısa bir səhiyyə anlayışı yaradın.

Statistikanı TƏKRAR ETMƏYİN.

Diqqət yetirin:
- trendlər
- mümkün səbəblər
- səhiyyə müşahidələri

Statistika:

Ən çox şikayət:
%s (%d hal)

Ən çox ziyarət edilən xəstəxana:
%s (%d ziyarət)

Yalnız anlayış mətnini qaytarın.
"""
                .formatted(
                        request.topComplaintType(),
                        request.topComplaintCount(),
                        request.topHospital(),
                        request.topHospitalVisitCount()
                );

        return callGemini(prompt);
    }

    private String callGemini(
            String prompt
    ) {

        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
                        + apiKey;

        Map<String, Object> requestBody =
                Map.of(
                        "contents",
                        List.of(
                                Map.of(
                                        "parts",
                                        List.of(
                                                Map.of(
                                                        "text",
                                                        prompt
                                                )
                                        )
                                )
                        )
                );

        GeminiResponse response =
                webClient
                        .post()
                        .uri(url)
                        .bodyValue(requestBody)
                        .retrieve()
                        .bodyToMono(
                                GeminiResponse.class
                        )
                        .block();
        return response
                .getCandidates()
                .get(0)
                .getContent()
                .getParts()
                .get(0)
                .getText();
    }
}


