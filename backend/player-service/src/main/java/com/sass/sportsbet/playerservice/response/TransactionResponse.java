package com.sass.sportsbet.playerservice.response;

import java.time.Instant;

import com.sass.sportsbet.playerservice.model.Transaction;

public record TransactionResponse(
        String id,
        String playerId,
        String type,
        double amount,
        String description,
        Instant createdAt,
        String betId
) {
    public static TransactionResponse from(Transaction tx) {
        return new TransactionResponse(
                tx.getId(),
                tx.getPlayerId(),
                tx.getType(),
                tx.getAmount(),
                tx.getDescription(),
                tx.getCreatedAt(),
                tx.getBetId()
        );
    }
}
