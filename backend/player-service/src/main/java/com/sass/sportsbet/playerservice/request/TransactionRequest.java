package com.sass.sportsbet.playerservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record TransactionRequest(
        @NotBlank String type,      // "DEBIT" or "CREDIT"
        @Positive double amount,
        String description,
        String betId
) {}
