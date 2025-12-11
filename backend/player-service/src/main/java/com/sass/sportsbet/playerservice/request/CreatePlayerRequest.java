package com.sass.sportsbet.playerservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePlayerRequest {
    @NotBlank(message = "Name is required.")
    private String name;
    
    @NotNull(message = "Balance amount is required.")
    @PositiveOrZero(message = "Initial balance must be >= 0.")
    private double initialBalance;
}

