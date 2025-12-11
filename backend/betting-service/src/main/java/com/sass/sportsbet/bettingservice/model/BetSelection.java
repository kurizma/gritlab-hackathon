package com.sass.sportsbet.bettingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BetSelection {

    private String matchId;   // UUID as string

    private Outcome outcome;  // HOME / DRAW / AWAY

    private double odds;      // locked odds at bet time
}
