package com.sass.sportsbet.bettingservice.request;

import com.sass.sportsbet.bettingservice.model.Outcome;

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
public class BetSelectionRequest {

    @NotBlank(message = "Match ID is required.")
    private String matchId;

    @NotNull(message = "Outcome is required.")
    private Outcome outcome;
}
