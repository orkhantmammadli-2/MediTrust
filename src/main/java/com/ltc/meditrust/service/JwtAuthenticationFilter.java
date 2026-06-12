package com.ltc.meditrust.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter
        implements GlobalFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {

        System.out.println("JWT FILTER CALLED");

        String path =
                exchange.getRequest()
                        .getURI()
                        .getPath();

        System.out.println("PATH = " + path);

        if (
                path.startsWith("/api/v1/auth")
                        ||
                        path.startsWith("/swagger-ui")
                        ||
                        path.startsWith("/v3/api-docs")
        ) {

            System.out.println("SKIP JWT CHECK");

            return chain.filter(exchange);
        }

        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

        System.out.println("AUTH HEADER = " + authHeader);

        if (
                authHeader == null
                        ||
                        !authHeader.startsWith("Bearer ")
        ) {

            System.out.println("TOKEN NOT FOUND");

            exchange.getResponse()
                    .setStatusCode(
                            HttpStatus.UNAUTHORIZED
                    );

            return exchange.getResponse()
                    .setComplete();
        }

        String token =
                authHeader.substring(7);

        System.out.println("TOKEN RECEIVED");

        boolean valid =
                jwtService.isValid(token);

        System.out.println(
                "TOKEN VALID = " + valid
        );

        if (!valid) {

            exchange.getResponse()
                    .setStatusCode(
                            HttpStatus.UNAUTHORIZED
                    );

            return exchange.getResponse()
                    .setComplete();
        }

        System.out.println(
                "REQUEST ALLOWED"
        );

        return chain.filter(exchange);
    }
}
