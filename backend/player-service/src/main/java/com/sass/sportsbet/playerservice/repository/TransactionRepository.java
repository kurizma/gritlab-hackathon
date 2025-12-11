package com.sass.sportsbet.playerservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sass.sportsbet.playerservice.model.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> findByPlayerIdOrderByCreatedAtDesc(String playerId);
}
