package com.sass.sportsbet.playerservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sass.sportsbet.playerservice.model.Player;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {
    Optional<Player> findByNormalizedName(String normalizedName);
}
