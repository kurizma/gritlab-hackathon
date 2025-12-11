package com.sass.sportsbet.bettingservice.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sass.sportsbet.bettingservice.client.MatchClient;
import com.sass.sportsbet.bettingservice.client.MatchResponse;
import com.sass.sportsbet.bettingservice.client.PlayerClient;
import com.sass.sportsbet.bettingservice.client.PlayerResponse;
import com.sass.sportsbet.bettingservice.client.TransactionRequest;
import com.sass.sportsbet.bettingservice.event.MatchFinishedEvent;
import com.sass.sportsbet.bettingservice.exception.InsufficientBalanceException;
import com.sass.sportsbet.bettingservice.model.Bet;
import com.sass.sportsbet.bettingservice.model.BetSelection;
import com.sass.sportsbet.bettingservice.model.BetStatus;
import com.sass.sportsbet.bettingservice.model.BetType;
import com.sass.sportsbet.bettingservice.model.MatchStatus;
import com.sass.sportsbet.bettingservice.repository.BetRepository;
import com.sass.sportsbet.bettingservice.request.BetSelectionRequest;
import com.sass.sportsbet.bettingservice.request.CombinationBetRequest;
import com.sass.sportsbet.bettingservice.request.SingleBetRequest;

@Service
public class BetService {

    private final BetRepository betRepository;
    private final PlayerClient playerClient;
    private final MatchClient matchClient;

    // Spring injects all three beans here
    public BetService(
            BetRepository betRepository,
            PlayerClient playerClient,
            MatchClient matchClient
    ) {
        this.betRepository = betRepository;
        this.playerClient = playerClient;
        this.matchClient = matchClient;
    }

    public Bet placeSingleBet(SingleBetRequest request) {
        // 1) Fetch player from player-service
        PlayerResponse player = playerClient.getPlayerByName(request.getPlayerName());

        // 2) Check balance >= stake
        if (player.balance() < request.getStake()) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // 3) Fetch match from match-service
        MatchResponse match = matchClient.getMatch(request.getMatchId());

        // 4) Reject if match has started or finished
        if (match.status() != MatchStatus.UPCOMING) {
            throw new IllegalStateException("Match already started or finished");
        }

        // 5) Choose correct odds based on outcome
        double odds = switch (request.getOutcome()) {
            case HOME -> match.homeTeamOdds();
            case DRAW -> match.drawOdds();
            case AWAY -> match.awayTeamOdds();
        };

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

        // 6) Save bet to get betId
        bet = betRepository.save(bet);

        // 7) Debit player
        TransactionRequest txReq = new TransactionRequest(
                "DEBIT",
                request.getStake(),
                "Stake for bet " + bet.getId(),
                bet.getId()
        );
        playerClient.createTransaction(player.id(), txReq);

        return bet;
    }

    public Bet placeCombinationBet(CombinationBetRequest request) {
        // 1) Fetch player
        PlayerResponse player = playerClient.getPlayerByName(request.getPlayerName());

        // 2) Check balance >= stake
        if (player.balance() < request.getStake()) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // 3) For each selection: fetch match, check status, pick correct odds
        List<BetSelection> selections = request.getSelections().stream()
                .map(this::toBetSelectionWithRealOdds)
                .collect(Collectors.toList());

        // 4) Multiply odds
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

        bet = betRepository.save(bet);

        TransactionRequest txReq = new TransactionRequest(
                "DEBIT",
                request.getStake(),
                "Stake for combo bet " + bet.getId(),
                bet.getId()
        );
        playerClient.createTransaction(player.id(), txReq);

        return bet;
    }

    // Helper: uses MatchClient + status check + correct odds
    private BetSelection toBetSelectionWithRealOdds(BetSelectionRequest req) {
        MatchResponse match = matchClient.getMatch(req.getMatchId());

        if (match.status() != MatchStatus.UPCOMING) {
            throw new IllegalStateException("One of the matches already started or finished");
        }

        double odds = switch (req.getOutcome()) {
            case HOME -> match.homeTeamOdds();
            case DRAW -> match.drawOdds();
            case AWAY -> match.awayTeamOdds();
        };

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

    public void settleBetsForMatch(MatchFinishedEvent event) {
        System.out.println("Settling bets for match " + event.matchId()
                + " with result " + event.result());
    }
}
