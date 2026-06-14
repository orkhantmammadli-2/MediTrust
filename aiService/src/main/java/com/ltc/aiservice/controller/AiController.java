package com.ltc.aiservice.controller;


import com.ltc.aiservice.dto.InsightRequest;
import com.ltc.aiservice.dto.InsightResponse;
import com.ltc.aiservice.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping ("/monthly-insight")
    public InsightResponse generate(
            @RequestBody
            InsightRequest request
    ) {

        return new InsightResponse(
                aiService.generateInsight(
                        request
                )
        );
    }
}