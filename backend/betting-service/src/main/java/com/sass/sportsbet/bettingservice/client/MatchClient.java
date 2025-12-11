package com.sass.sportsbet.bettingservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MatchClient {

    private final RestTemplate restTemplate;

    public MatchClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MatchResponse getMatch(String matchId) {
        return restTemplate.getForObject(
                "http://localhost:8080/matches/" + matchId,
                MatchResponse.class
        );
    }
}
