package com.sass.sportsbet.bettingservice.messaging;

import com.sass.sportsbet.bettingservice.event.MatchFinishedEvent;
import com.sass.sportsbet.bettingservice.service.BetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MatchFinishedListener {

    private static final Logger log = LoggerFactory.getLogger(MatchFinishedListener.class);

    private final BetService betService;

    public MatchFinishedListener(BetService betService) {
        this.betService = betService;
    }

    @KafkaListener(topics = "match-finished", groupId = "betting-service")
    public void handleMatchFinished(MatchFinishedEvent event) {
        log.info("Received MatchFinishedEvent: matchId={}, result={}",
                event.matchId(), event.result());
        betService.settleBetsForMatch(event);
    }
}
