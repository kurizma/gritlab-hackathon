package com.sass.sportsbet.bettingservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sass.sportsbet.bettingservice.model.Bet;
import com.sass.sportsbet.bettingservice.model.BetStatus;

@Repository
public interface BetRepository extends MongoRepository<Bet, String> {
    List<Bet> findByPlayerNameIgnoreCase(String playerName);

    List<Bet> findByPlayerNameIgnoreCaseAndStatus(String playerName, BetStatus status);
}
