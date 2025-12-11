package com.sass.sportsbet.matchservice.service;

import org.springframework.stereotype.Service;

import com.sass.sportsbet.matchservice.model.MatchModel;
import com.sass.sportsbet.matchservice.model.MatchSeedDTO;
import com.sass.sportsbet.matchservice.model.MatchStatus;
import com.sass.sportsbet.matchservice.repository.MatchRepository;

import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    // used by GET /matches
    public List<MatchModel> getAllMatches() {
        return matchRepository.findAll();
    }

    // used by GET /matches/finished
    public List<MatchModel> getFinishedMatches() {
        return matchRepository.findByStatus(MatchStatus.FINISHED);
    }

    public void seedMatchesIfEmpty(List<MatchSeedDTO> seeds, long startupTime) {
        if (matchRepository.count() > 0) return;

        for (MatchSeedDTO dto: seeds) {
            MatchModel m = new MatchModel(dto.home_team, dto.away_team);
            m.setHomeTeamOdds(dto.home_team_odds);
            m.setAwayTeamOdds(dto.away_team_odds);
            m.setDrawOdds(dto.draw_odds);
            m.setMatchDuration(dto.match_duration);
            m.setStartTimeDelay(dto.start_time_delay); 
            
            long start = startupTime + dto.start_time_delay * 1000L;
            long end = start + dto.match_duration * 1000L;
            m.setKickoffAt(start);
            m.setEndsAt(end);
            m.setStartTime(start);
            m.setEndTime(end);
            m.setStatus(MatchStatus.UPCOMING);

            matchRepository.save(m); //Save to Mongo DB
        }
    }

    public void clearAllMatches() {
        matchRepository.deleteAll();
    }

}
