package com.sass.sportsbet.playerservice.response;


import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerResponse {
    private String id;
    private String name;
    private double balance;
    private Instant createdAt;
}

