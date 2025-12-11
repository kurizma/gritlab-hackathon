package com.sass.sportsbet.bettingservice.client;

import com.sass.sportsbet.bettingservice.model.MatchStatus;

public record MatchResponse(
        String id,
        String homeTeam,
        String awayTeam,
        double homeTeamOdds,
        double awayTeamOdds,
        double drawOdds,
        int matchDuration,
        int startTimeDelay,
        MatchStatus status,
        long startTime,
        long endTime
) {}
