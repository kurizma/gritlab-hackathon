package com.sass.sportsbet.matchservice.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "matches")

public class MatchModel {
  @Id
  private String id;
  private String homeTeam;
  private String awayTeam;

  private double homeTeamOdds;
  private double awayTeamOdds;
  private double drawOdds; 
  private int matchDuration;
  private int startTimeDelay;
  private MatchStatus status;
  private long startTime;
  private long endTime;

  private long kickoffAt;
  private long endsAt;

 
    public MatchModel() {}

    public MatchModel(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }  

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }   

    public double getHomeTeamOdds() {
        return homeTeamOdds;
    }

    public void setHomeTeamOdds(double homeTeamOdds) {
        this.homeTeamOdds = homeTeamOdds;
    }

    public double getAwayTeamOdds() {
        return awayTeamOdds;
    }

    public void setAwayTeamOdds(double awayTeamOdds) {
        this.awayTeamOdds = awayTeamOdds;
    }

    public double getDrawOdds() {
        return drawOdds;
    }

    public void setDrawOdds(double drawOdds) {
        this.drawOdds = drawOdds;
    }

    public int getMatchDuration() {
        return matchDuration;
    }

    public void setMatchDuration(int matchDuration) {
        this.matchDuration = matchDuration;
    }

    public int getStartTimeDelay() {
        return startTimeDelay;
    }

    public void setStartTimeDelay(int startTimeDelay) {
        this.startTimeDelay = startTimeDelay;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getKickoffAt() {
        return kickoffAt;
    }

    public void setKickoffAt(long kickoffAt) {
        this.kickoffAt = kickoffAt;
    }

    public long getEndsAt() {
        return endsAt;
    }   

    public void setEndsAt(long endsAt) {
        this.endsAt = endsAt;
    }

}