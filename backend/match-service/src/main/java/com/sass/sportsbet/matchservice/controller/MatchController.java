package com.sass.sportsbet.matchservice.controller;

import com.sass.sportsbet.matchservice.model.MatchModel;
import com.sass.sportsbet.matchservice.service.MatchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}


