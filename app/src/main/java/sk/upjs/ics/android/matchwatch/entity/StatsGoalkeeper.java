package sk.upjs.ics.android.matchwatch.entity;


import java.io.Serializable;

public class StatsGoalkeeper implements Serializable {

    // Goalkeepers: GPT, GKD, GPI, MIP, MIP%, GA, SVS, SOG, SVS%, GAA, SO, W, L
    private int gamePlayedByTeam;

    private int goals;

    private int assists;

    private int goalkeeperDressed;

    private int goalkeeperTotalPlayed;

    private String totalMinutesPlayed; //MM:SS

    private float minutesAsPercentage;

    private long penaltiesInMinutes;

    private int saves;

    private int shotsOnGoal;

    private float percentageSaves;

    private float avgGoals;

    private int shutouts;

    private int gamesWon;

    private int gamesLost;

    //constructors
    public StatsGoalkeeper() {
    }

    public StatsGoalkeeper(int gamePlayedByTeam, int goals, int assists, int goalkeeperDressed, int goalkeeperTotalPlayed, String totalMinutesPlayed, float minutesAsPercentage, long penaltiesInMinutes, int saves, int shotsOnGoal, float percentageSaves, float avgGoals, int shutouts, int gamesWon, int gamesLost) {
        this.gamePlayedByTeam = gamePlayedByTeam;
        this.goals = goals;
        this.assists = assists;
        this.goalkeeperDressed = goalkeeperDressed;
        this.goalkeeperTotalPlayed = goalkeeperTotalPlayed;
        this.totalMinutesPlayed = totalMinutesPlayed;
        this.minutesAsPercentage = minutesAsPercentage;
        this.penaltiesInMinutes = penaltiesInMinutes;
        this.saves = saves;
        this.shotsOnGoal = shotsOnGoal;
        this.percentageSaves = percentageSaves;
        this.avgGoals = avgGoals;
        this.shutouts = shutouts;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
    }

    // getters & setters
    public int getGamePlayedByTeam() {
        return gamePlayedByTeam;
    }

    public void setGamePlayedByTeam(int gamePlayedByTeam) {
        this.gamePlayedByTeam = gamePlayedByTeam;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getGoalkeeperDressed() {
        return goalkeeperDressed;
    }

    public void setGoalkeeperDressed(int goalkeeperDressed) {
        this.goalkeeperDressed = goalkeeperDressed;
    }

    public int getGoalkeeperTotalPlayed() {
        return goalkeeperTotalPlayed;
    }

    public void setGoalkeeperTotalPlayed(int goalkeeperTotalPlayed) {
        this.goalkeeperTotalPlayed = goalkeeperTotalPlayed;
    }

    public String getTotalMinutesPlayed() {
        return totalMinutesPlayed;
    }

    public void setTotalMinutesPlayed(String totalMinutesPlayed) {
        this.totalMinutesPlayed = totalMinutesPlayed;
    }

    public float getMinutesAsPercentage() {
        return minutesAsPercentage;
    }

    public void setMinutesAsPercentage(float minutesAsPercentage) {
        this.minutesAsPercentage = minutesAsPercentage;
    }

    public long getPenaltiesInMinutes() {
        return penaltiesInMinutes;
    }

    public void setPenaltiesInMinutes(long penaltiesInMinutes) {
        this.penaltiesInMinutes = penaltiesInMinutes;
    }

    public int getSaves() {
        return saves;
    }

    public void setSaves(int saves) {
        this.saves = saves;
    }

    public int getShotsOnGoal() {
        return shotsOnGoal;
    }

    public void setShotsOnGoal(int shotsOnGoal) {
        this.shotsOnGoal = shotsOnGoal;
    }

    public float getPercentageSaves() {
        return percentageSaves;
    }

    public void setPercentageSaves(float percentageSaves) {
        this.percentageSaves = percentageSaves;
    }

    public float getAvgGoals() {
        return avgGoals;
    }

    public void setAvgGoals(float avgGoals) {
        this.avgGoals = avgGoals;
    }

    public int getShutouts() {
        return shutouts;
    }

    public void setShutouts(int shutouts) {
        this.shutouts = shutouts;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }
}
