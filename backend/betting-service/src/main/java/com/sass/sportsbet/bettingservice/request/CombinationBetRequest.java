package com.sass.sportsbet.bettingservice.request;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CombinationBetRequest {

    @NotBlank(message = "Player name is required.")
    private String playerName;

    @NotEmpty(message = "At least two selections are required.")
    private List<BetSelectionRequest> selections;   // minItems = 2

    @Min(value = 1, message = "Stake must be at least 0.01.")
    private double stake;
}
