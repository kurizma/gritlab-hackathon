package com.sass.sportsbet.bettingservice.event;

public record MatchFinishedEvent(
        String matchId,
        String result // "HOME", "DRAW", "AWAY"
) {}
