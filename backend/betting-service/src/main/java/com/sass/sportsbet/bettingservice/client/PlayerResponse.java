package com.sass.sportsbet.bettingservice.client;

import java.time.Instant;

public record PlayerResponse(
        String id,
        String name,
        double balance,
        Instant createdAt
) {}
