package com.sass.sportsbet.matchservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.sass.sportsbet.matchservice.model.MatchModel;
import com.sass.sportsbet.matchservice.model.MatchStatus;


public interface MatchRepository extends MongoRepository<MatchModel, String> {
    List<MatchModel> findByStatus(MatchStatus status);
}
