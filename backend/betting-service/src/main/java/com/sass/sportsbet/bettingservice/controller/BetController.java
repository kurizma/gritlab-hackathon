package com.sass.sportsbet.bettingservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sass.sportsbet.bettingservice.model.Bet;
import com.sass.sportsbet.bettingservice.model.BetStatus;
import com.sass.sportsbet.bettingservice.request.CombinationBetRequest;
import com.sass.sportsbet.bettingservice.request.SingleBetRequest;
import com.sass.sportsbet.bettingservice.service.BetService;

import jakarta.validation.Valid;

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
    // ----------------- // 
    @GetMapping("/players/{playerName}/bets")
    public ResponseEntity<List<Bet>> getPlayerBets(
        @PathVariable String playerName,
        @RequestParam(name = "status", required = false) BetStatus status) {
    List<Bet> bets = betService.getBetsForPlayer(playerName, status);
    return ResponseEntity.ok(bets);
}
}
