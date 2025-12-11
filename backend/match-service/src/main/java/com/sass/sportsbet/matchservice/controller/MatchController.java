package com.sass.sportsbet.matchservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sass.sportsbet.matchservice.model.MatchModel;
import com.sass.sportsbet.matchservice.service.MatchService;

@RestController
@RequestMapping("/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    // GET /matches
    @GetMapping
    public List<MatchModel> getMatches() {
        return matchService.getAllMatches();
    }

    // GET /matches/finished
    @GetMapping("/finished")
    public List<MatchModel> getFinishedMatches() {
        return matchService.getFinishedMatches();
    }

    @GetMapping("/{matchId}")
    public MatchModel getMatchById(@PathVariable String matchId) {
        return matchService.getMatchById(matchId);
    }

}


