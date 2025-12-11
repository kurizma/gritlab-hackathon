package com.sass.sportsbet.bettingservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sass.sportsbet.bettingservice.model.Bet;

@Repository
public interface BetRepository extends MongoRepository<Bet, String> {
}
