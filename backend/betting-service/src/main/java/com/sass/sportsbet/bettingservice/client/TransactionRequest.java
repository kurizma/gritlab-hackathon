package com.sass.sportsbet.bettingservice.client;

public record TransactionRequest(
        String type,      // "DEBIT" or "CREDIT"
        double amount,
        String description,
        String betId
) {}
