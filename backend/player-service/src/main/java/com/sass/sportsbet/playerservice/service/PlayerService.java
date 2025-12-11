package com.sass.sportsbet.playerservice.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.sass.sportsbet.playerservice.exception.PlayerAlreadyExistsException;
import com.sass.sportsbet.playerservice.exception.PlayerNotFoundException;
import com.sass.sportsbet.playerservice.model.Player;
import com.sass.sportsbet.playerservice.repository.PlayerRepository;
import com.sass.sportsbet.playerservice.request.CreatePlayerRequest;
import com.sass.sportsbet.playerservice.response.PlayerResponse;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public PlayerResponse createPlayer(CreatePlayerRequest request) {
        String normalizedName = request.getName().toLowerCase();

        playerRepository.findByNormalizedName(normalizedName)
                .ifPresent(existing -> {
                    throw new PlayerAlreadyExistsException(request.getName());
                });

        Player player = Player.builder()
                .name(request.getName())
                .normalizedName(normalizedName)
                .balance(request.getInitialBalance())
                .createdAt(Instant.now())
                .build();

        Player saved = playerRepository.save(player);
        return toResponse(saved);
    }

    public PlayerResponse getByName(String name) {
        String normalizedName = name.toLowerCase();

        Player player = playerRepository.findByNormalizedName(normalizedName)
                .orElseThrow(() -> new PlayerNotFoundException(name));

        return toResponse(player);
    }

    private PlayerResponse toResponse(Player player) {
        return PlayerResponse.builder()
                .id(player.getId())
                .name(player.getName())
                .balance(player.getBalance())
                .createdAt(player.getCreatedAt())
                .build();
    }
}
