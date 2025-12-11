package com.sass.sportsbet.playerservice.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sass.sportsbet.playerservice.exception.PlayerAlreadyExistsException;
import com.sass.sportsbet.playerservice.exception.PlayerNotFoundException;
import com.sass.sportsbet.playerservice.model.Player;
import com.sass.sportsbet.playerservice.model.Transaction;
import com.sass.sportsbet.playerservice.repository.PlayerRepository;
import com.sass.sportsbet.playerservice.repository.TransactionRepository;
import com.sass.sportsbet.playerservice.request.CreatePlayerRequest;
import com.sass.sportsbet.playerservice.request.TransactionRequest;
import com.sass.sportsbet.playerservice.response.PlayerResponse;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TransactionRepository transactionRepository;

    public PlayerService(PlayerRepository playerRepository,
            TransactionRepository transactionRepository) {
        this.playerRepository = playerRepository;
        this.transactionRepository = transactionRepository;
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

    // ------------------- //
    public Transaction createTransaction(String playerId, TransactionRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        double newBalance = switch (request.type()) {
            case "DEBIT" -> {
                if (player.getBalance() < request.amount()) {
                    throw new IllegalStateException("Insufficient balance");
                }
                yield player.getBalance() - request.amount();
            }
            case "CREDIT" -> player.getBalance() + request.amount();
            default -> throw new IllegalArgumentException("Unknown transaction type");
        };

        player.setBalance(newBalance);
        playerRepository.save(player);

        Transaction tx = Transaction.builder()
                .playerId(playerId)
                .type(request.type())
                .amount(request.amount())
                .description(request.description())
                .createdAt(Instant.now())
                .betId(request.betId())
                .build();

        return transactionRepository.save(tx);
    }
    public List<Transaction> getTransactionsForPlayer(String playerId) {
        return transactionRepository.findByPlayerIdOrderByCreatedAtDesc(playerId);
    }

}
