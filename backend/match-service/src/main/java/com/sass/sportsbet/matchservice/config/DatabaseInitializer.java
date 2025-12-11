package com.sass.sportsbet.matchservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoTemplate;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;

    public DatabaseInitializer(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) {
        // Option A: drop whole database (removes collections + indexes)
        // mongoTemplate.getDb().dropDatabase();

        // Option B: drop only the collections you use
        mongoTemplate.dropCollection("matches");
        // mongoTemplate.dropCollection("players");
        // mongoTemplate.dropCollection("bets");
        // mongoTemplate.dropCollection("transactions");

    }
}
