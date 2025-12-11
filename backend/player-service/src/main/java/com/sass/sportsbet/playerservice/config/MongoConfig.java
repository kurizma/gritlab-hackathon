package com.sass.sportsbet.playerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

    private static final String CONNECTION_STRING =
            "mongodb+srv://Cluster05093:.qwe.qwe.qwe@cluster05093.dh3rmhg.mongodb.net/gritlab-hackathon?retryWrites=true&w=majority&appName=Cluster05093";

    private static final String DATABASE_NAME = "gritlab-hackathon";

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(CONNECTION_STRING);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, DATABASE_NAME);
    }
}
