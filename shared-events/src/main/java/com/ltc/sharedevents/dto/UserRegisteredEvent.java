package com.ltc.sharedevents.dto;

public record UserRegisteredEvent(
        String email,
        String name
) {
}