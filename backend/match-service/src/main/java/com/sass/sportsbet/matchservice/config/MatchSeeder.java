package com.sass.sportsbet.matchservice.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.sass.sportsbet.matchservice.service.MatchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sass.sportsbet.matchservice.model.MatchSeedDTO;

import java.io.InputStream;
import java.util.List;

@Component
public class MatchSeeder implements ApplicationRunner {

    private final MatchService matchService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MatchSeeder(MatchService matchService) {
        this.matchService = matchService;
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
}
