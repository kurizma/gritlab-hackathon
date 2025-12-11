package com.sass.sportsbet.matchservice.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.sass.sportsbet.matchservice.service.MatchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sass.sportsbet.matchservice.model.MatchSeedDTO;
import com.sass.sportsbet.matchservice.model.MatchStatus;
import com.sass.sportsbet.matchservice.model.MatchModel;
import com.sass.sportsbet.matchservice.repository.MatchRepository;

import org.springframework.scheduling.annotation.Scheduled;

import java.io.InputStream;
import java.util.List;

@Component
public class MatchSeeder implements ApplicationRunner {

    private final MatchService matchService;
    private final MatchRepository matchRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MatchSeeder(MatchService matchService, MatchRepository matchRepository) {
        this.matchService = matchService;
        this.matchRepository = matchRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception { 
        ClassPathResource resource = new ClassPathResource("football_matches.json");
        try (InputStream is = resource.getInputStream()) {
            List<MatchSeedDTO> seeds = objectMapper.readValue(
                    is, new TypeReference<List<MatchSeedDTO>>() {});
            long startupTime = System.currentTimeMillis();
            matchService.seedMatchesIfEmpty(seeds, startupTime);
        }
    }

    @Scheduled(fixedRate = 10_000) //Run every 10 seconds
    public void updateMatchStatuses() {
        long now = System.currentTimeMillis();
        System.out.println("Scheduler tick: " + now);

        // UPCOMING --> IN_PROGRESS
        var upcoming = matchRepository.findByStatus(MatchStatus.UPCOMING);
        for (MatchModel m: upcoming) {
            if (now >= m.getStartTime() && now < m.getEndTime()) {
                m.setStatus(MatchStatus.IN_PROGRESS);
                matchRepository.save(m);
            }
        } 
        
        // IN_PROGRESS --> FINISHED
        var inProgress = matchRepository.findByStatus(MatchStatus.IN_PROGRESS);
        for (MatchModel m: inProgress) {
            if (now >= m.getEndTime()) {
                m.setStatus(MatchStatus.FINISHED);
                matchRepository.save(m);  
            }
        }   
    }
}
