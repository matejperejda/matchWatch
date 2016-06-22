package sk.upjs.ics.android.matchwatch.entity;


import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Match implements Serializable {

    private Date gameDate;

    private Team homeTeam;

    private Team awayTeam;

    private int scoreHome;

    private int scoreAway;

    private List<Person> referees;

    private List<Person> linesmen;

    private String matchImage;

    public String getMatchImage() {
        return matchImage;
    }

    public void setMatchImage(String matchImage) {
        this.matchImage = matchImage;
    }

    // constructors
    public Match() {
    }

    public Match(Date gameDate, Team homeTeam, Team awayTeam, int homeScore, int awayScore, List<Person> referees, List<Person> linesmen) {
        this.gameDate = gameDate;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.scoreHome = homeScore;
        this.scoreAway = awayScore;
        this.referees = referees;
        this.linesmen = linesmen;
    }

    // getters & setters
    public Date getGameDate() {
        return gameDate;
    }

    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public List<Person> getReferees() {
        return referees;
    }

    public void setReferees(List<Person> referees) {
        this.referees = referees;
    }

    public List<Person> getLinesmen() {
        return linesmen;
    }

    public void setLinesmen(List<Person> linesmen) {
        this.linesmen = linesmen;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getScoreHome() {
        return scoreHome;
    }

    public void setScoreHome(int scoreHome) {
        this.scoreHome = scoreHome;
    }

    public int getScoreAway() {
        return scoreAway;
    }

    public void setScoreAway(int scoreAway) {
        this.scoreAway = scoreAway;
    }
}
