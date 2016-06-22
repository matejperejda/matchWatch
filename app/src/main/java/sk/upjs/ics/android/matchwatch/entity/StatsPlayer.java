package sk.upjs.ics.android.matchwatch.entity;


import java.io.Serializable;

public class StatsPlayer implements Serializable {

    // Players: GP, G, A, PTS, PIM, +/-, GGWG, PPG, SHG, SOG, SG%, TM, M/G, TSh, AT/S
    private int gamesPlayed;

    private int goals;

    private int assists;

    private int points;

    private long penaltiesInMinutes;

    private int plusesMinues;

    private int gameWinningG;

    private int powerPlayG;

    private int shorthandedG;

    private int shotsOnGoal;

    private float percentageShots;

    private String totalMinutesPlayed; //MM:SS

    private String minutesPerGame; //MM:SS

    private int totalShiftsPlayed;

    private String avgTimePerShift; //MM:SS

    // constructors
    public StatsPlayer() {

    }

    public StatsPlayer(int gamesPlayed, int goals, int assists, int points, long penaltiesInMinutes, int plusesMinues, int gameWinningG, int powerPlayG, int shorthandedG, int shotsOnGoal, float percentageShots, String totalMinutesPlayed, String minutesPerGame, int totalShiftsPlayed, String avgTimePerShift) {
        this.gamesPlayed = gamesPlayed;
        this.goals = goals;
        this.assists = assists;
        this.points = points;
        this.penaltiesInMinutes = penaltiesInMinutes;
        this.plusesMinues = plusesMinues;
        this.gameWinningG = gameWinningG;
        this.powerPlayG = powerPlayG;
        this.shorthandedG = shorthandedG;
        this.shotsOnGoal = shotsOnGoal;
        this.percentageShots = percentageShots;
        this.totalMinutesPlayed = totalMinutesPlayed;
        this.minutesPerGame = minutesPerGame;
        this.totalShiftsPlayed = totalShiftsPlayed;
        this.avgTimePerShift = avgTimePerShift;
    }

    // getters & setters
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public long getPenaltiesInMinutes() {
        return penaltiesInMinutes;
    }

    public void setPenaltiesInMinutes(long penaltiesInMinutes) {
        this.penaltiesInMinutes = penaltiesInMinutes;
    }

    public int getPlusesMinues() {
        return plusesMinues;
    }

    public void setPlusesMinues(int plusesMinues) {
        this.plusesMinues = plusesMinues;
    }

    public int getGameWinningG() {
        return gameWinningG;
    }

    public void setGameWinningG(int gameWinningG) {
        this.gameWinningG = gameWinningG;
    }

    public int getPowerPlayG() {
        return powerPlayG;
    }

    public void setPowerPlayG(int powerPlayG) {
        this.powerPlayG = powerPlayG;
    }

    public int getShorthandedG() {
        return shorthandedG;
    }

    public void setShorthandedG(int shorthandedG) {
        this.shorthandedG = shorthandedG;
    }

    public int getShotsOnGoal() {
        return shotsOnGoal;
    }

    public void setShotsOnGoal(int shotsOnGoal) {
        this.shotsOnGoal = shotsOnGoal;
    }

    public float getPercentageShots() {
        return percentageShots;
    }

    public void setPercentageShots(float percentageShots) {
        this.percentageShots = percentageShots;
    }

    public String getTotalMinutesPlayed() {
        return totalMinutesPlayed;
    }

    public void setTotalMinutesPlayed(String totalMinutesPlayed) {
        this.totalMinutesPlayed = totalMinutesPlayed;
    }

    public String getMinutesPerGame() {
        return minutesPerGame;
    }

    public void setMinutesPerGame(String minutesPerGame) {
        this.minutesPerGame = minutesPerGame;
    }

    public int getTotalShiftsPlayed() {
        return totalShiftsPlayed;
    }

    public void setTotalShiftsPlayed(int totalShiftsPlayed) {
        this.totalShiftsPlayed = totalShiftsPlayed;
    }

    public String getAvgTimePerShift() {
        return avgTimePerShift;
    }

    public void setAvgTimePerShift(String avgTimePerShift) {
        this.avgTimePerShift = avgTimePerShift;
    }
}
