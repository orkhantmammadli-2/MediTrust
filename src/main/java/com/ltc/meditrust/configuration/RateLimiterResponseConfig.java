package com.ltc.meditrust.configuration;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterResponseConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(1, 1) {
            @Override
            public Mono<Response> isAllowed(String routeId, String id) {
                return super.isAllowed(routeId, id)
                        .flatMap(response -> {
                            if (!response.isAllowed()) {
                                return Mono.error(new RuntimeException("RATE_LIMIT_EXCEEDED"));
                            }
                            return Mono.just(response);
                        });
            }
        };
    }
}
