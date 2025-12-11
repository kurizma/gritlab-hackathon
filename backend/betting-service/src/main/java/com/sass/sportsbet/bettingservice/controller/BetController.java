package com.sass.sportsbet.bettingservice.controller;

import com.sass.sportsbet.bettingservice.model.Bet;
import com.sass.sportsbet.bettingservice.request.CombinationBetRequest;
import com.sass.sportsbet.bettingservice.request.SingleBetRequest;
import com.sass.sportsbet.bettingservice.service.BetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bets")
public class BetController {

    private final BetService betService;

    public BetController(BetService betService) {
        this.betService = betService;
    }

    // POST /bets/single -> 201 with Bet
    @PostMapping("/single")
    public ResponseEntity<Bet> placeSingleBet(
            @Valid @RequestBody SingleBetRequest request) {
        Bet bet = betService.placeSingleBet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bet);
    }

    // POST /bets/combination -> 201 with Bet
    @PostMapping("/combination")
    public ResponseEntity<Bet> placeCombinationBet(
            @Valid @RequestBody CombinationBetRequest request) {
        Bet bet = betService.placeCombinationBet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bet);
    }
}
