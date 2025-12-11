package com.sass.sportsbet.bettingservice.request;

import com.sass.sportsbet.bettingservice.model.Outcome;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingleBetRequest {

    @NotBlank(message = "Player name is required.")
    private String playerName;

    @NotBlank(message = "Match ID is required.")
    private String matchId;      // UUID as string

    @NotNull(message = "Outcome is required.")
    private Outcome outcome;     // HOME / DRAW / AWAY

    @Min(value = 1, message = "Stake must be at least 0.01.")
    private double stake;
}
