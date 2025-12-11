package com.sass.sportsbet.bettingservice.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sass.sportsbet.bettingservice.client.PlayerClient;
import com.sass.sportsbet.bettingservice.client.PlayerResponse;
import com.sass.sportsbet.bettingservice.exception.InsufficientBalanceException;
import com.sass.sportsbet.bettingservice.model.Bet;
import com.sass.sportsbet.bettingservice.model.BetSelection;
import com.sass.sportsbet.bettingservice.model.BetStatus;
import com.sass.sportsbet.bettingservice.model.BetType;
import com.sass.sportsbet.bettingservice.repository.BetRepository;
import com.sass.sportsbet.bettingservice.request.BetSelectionRequest;
import com.sass.sportsbet.bettingservice.request.CombinationBetRequest;
import com.sass.sportsbet.bettingservice.request.SingleBetRequest;

@Service
public class BetService {

    private final BetRepository betRepository;
    private final PlayerClient playerClient;

    // Constructor injection: Spring will pass both beans here
    public BetService(BetRepository betRepository, PlayerClient playerClient) {
        this.betRepository = betRepository;
        this.playerClient = playerClient;
    }

    public Bet placeSingleBet(SingleBetRequest request) {
        // 1) Fetch player from player-service
        PlayerResponse player = playerClient.getPlayerByName(request.getPlayerName());

        // 2) Check balance >= stake (no transaction yet, just validation)
        if (player.balance() < request.getStake()) {
            throw new IllegalStateException("Insufficient balance");
        }

        double odds = 2.0; // placeholder for now

        BetSelection selection = BetSelection.builder()
                .matchId(request.getMatchId())
                .outcome(request.getOutcome())
                .odds(odds)
                .build();

        double finalOdds = odds;
        double potentialPayout = request.getStake() * finalOdds;

        Bet bet = Bet.builder()
                .playerName(request.getPlayerName())
                .type(BetType.SINGLE)
                .status(BetStatus.PLACED)
                .stake(request.getStake())
                .finalOdds(finalOdds)
                .potentialPayout(potentialPayout)
                .winAmount(null)
                .placedAt(Instant.now())
                .selections(List.of(selection))
                .build();

        return betRepository.save(bet);
    }

        public Bet placeCombinationBet(CombinationBetRequest request) {
        // 1) Fetch player from player-service
        PlayerResponse player = playerClient.getPlayerByName(request.getPlayerName());

        // 2) Check balance >= stake
        if (player.balance() < request.getStake()) {
                throw new InsufficientBalanceException("Insufficient balance");
        }

        // 3) Build selections with placeholder odds
        List<BetSelection> selections = request.getSelections().stream()
                .map(this::toBetSelectionPlaceholderOdds)
                .collect(Collectors.toList());

        double finalOdds = selections.stream()
                .mapToDouble(BetSelection::getOdds)
                .reduce(1.0, (a, b) -> a * b);

        double potentialPayout = request.getStake() * finalOdds;

        Bet bet = Bet.builder()
                .playerName(request.getPlayerName())
                .type(BetType.COMBINATION)
                .status(BetStatus.PLACED)
                .stake(request.getStake())
                .finalOdds(finalOdds)
                .potentialPayout(potentialPayout)
                .winAmount(null)
                .placedAt(Instant.now())
                .selections(selections)
                .build();

        return betRepository.save(bet);
        }


    private BetSelection toBetSelectionPlaceholderOdds(BetSelectionRequest req) {
        double odds = 2.0;
        return BetSelection.builder()
                .matchId(req.getMatchId())
                .outcome(req.getOutcome())
                .odds(odds)
                .build();
    }
    public List<Bet> getBetsForPlayer(String playerName, BetStatus status) {
    if (status == null) {
        return betRepository.findByPlayerNameIgnoreCase(playerName);
    }
    return betRepository.findByPlayerNameIgnoreCaseAndStatus(playerName, status);
}

}
