package com.sass.sportsbet.bettingservice.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PlayerClient {

    private final RestClient restClient;

    public PlayerClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8082") // player-service port
                .build();
    }

    public PlayerResponse getPlayerByName(String playerName) {
        return restClient.get()
                .uri("/players/{playerName}", playerName)
                .retrieve()
                .body(PlayerResponse.class);
    }
    public void createTransaction(String playerId, TransactionRequest request) {
        restClient.post()
                .uri("/players/{playerId}/transactions", playerId)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
