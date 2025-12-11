package com.sass.sportsbet.playerservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sass.sportsbet.playerservice.model.Transaction;
import com.sass.sportsbet.playerservice.request.CreatePlayerRequest;
import com.sass.sportsbet.playerservice.request.TransactionRequest;
import com.sass.sportsbet.playerservice.response.PlayerResponse;
import com.sass.sportsbet.playerservice.response.TransactionResponse;
import com.sass.sportsbet.playerservice.service.PlayerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // POST /players  -> 201 with Player
    @PostMapping
    public ResponseEntity<PlayerResponse> createPlayer(
            @Valid @RequestBody CreatePlayerRequest request) {
        PlayerResponse response = playerService.createPlayer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /players/{playerName}  -> 200 with Player
    @GetMapping("/{playerName}")
    public ResponseEntity<PlayerResponse> getPlayer(
            @PathVariable String playerName) {
        PlayerResponse response = playerService.getByName(playerName);
        return ResponseEntity.ok(response);
    }

    // ------------ //

    @PostMapping("/{playerId}/transactions")
    public ResponseEntity<TransactionResponse> createTransaction(
            @PathVariable String playerId,
            @Valid @RequestBody TransactionRequest request) {
        Transaction tx = playerService.createTransaction(playerId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TransactionResponse.from(tx));
    }

    @GetMapping("/{playerId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @PathVariable String playerId) {
        List<TransactionResponse> list = playerService.getTransactionsForPlayer(playerId)
                .stream()
                .map(TransactionResponse::from)
                .toList();
        return ResponseEntity.ok(list);
    }

}
