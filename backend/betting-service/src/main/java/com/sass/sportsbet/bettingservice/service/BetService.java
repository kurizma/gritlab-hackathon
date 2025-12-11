package com.sass.sportsbet.bettingservice.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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

    public BetService(BetRepository betRepository) {
        this.betRepository = betRepository;
    }

    public Bet placeSingleBet(SingleBetRequest request) {
        // TODO: validate player + match via REST calls, check balance, get real odds
        double odds = 2.0; // placeholder

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
        // TODO: validate player + all matches via REST, check balance, get real odds for each selection

        // For now, use a placeholder odds (e.g. 2.0) per selection
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
        double odds = 2.0; // placeholder until you call match-service for real odds
        return BetSelection.builder()
                .matchId(req.getMatchId())
                .outcome(req.getOutcome())
                .odds(odds)
                .build();
    }
}
