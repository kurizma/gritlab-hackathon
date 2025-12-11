package com.sass.sportsbet.bettingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bets")
public class Bet {

    @Id
    private String id;

    private String playerName;

    private BetType type;          // SINGLE or COMBINATION

    private BetStatus status;      // PLACED / WON / LOST

    private double stake;

    private double finalOdds;      // single: selection odds; combo: product of odds

    private double potentialPayout;

    private Double winAmount;      // nullable; set when settled

    private Instant placedAt;

    private Instant settledAt;     // nullable

    private List<BetSelection> selections;
}
