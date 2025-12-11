package com.sass.sportsbet.playerservice.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private String playerId;
    private String type;        // "DEBIT" or "CREDIT"
    private double amount;
    private String description;
    private Instant createdAt;
    private String betId;       // optional: link to bet
}
