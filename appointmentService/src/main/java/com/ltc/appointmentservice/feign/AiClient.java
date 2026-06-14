package com.ltc.appointmentservice.feign;

import com.ltc.sharedevents.dto.InsightRequest;
import com.ltc.sharedevents.dto.InsightResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "ai-service",
        url = "http://localhost:8085"
)
public interface AiClient {

    @PostMapping(
            "/api/v1/ai/monthly-insight"
    )
    InsightResponse generateInsight(
            @RequestBody
            InsightRequest request
    );
}
