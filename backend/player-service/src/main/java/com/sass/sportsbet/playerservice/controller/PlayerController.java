package com.sass.sportsbet.playerservice.controller;

import com.sass.sportsbet.playerservice.request.CreatePlayerRequest;
import com.sass.sportsbet.playerservice.response.PlayerResponse;
import com.sass.sportsbet.playerservice.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
