package sk.upjs.ics.android.matchwatch.dbsEntity;

import java.io.Serializable;

public class MatchDatabase implements Serializable {

    private int matchId;

    private String gameDate;

    private int homeId;

    private String homeName;

    private String homeShort;

    private int homeScore;

    private int awayId;

    private String awayName;

    private String awayShort;

    private int awayScore;

    private int isFinished;

    private byte[] matchImage;

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public int getHomeId() {
        return homeId;
    }

    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getHomeShort() {
        return homeShort;
    }

    public void setHomeShort(String homeShort) {
        this.homeShort = homeShort;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayId() {
        return awayId;
    }

    public void setAwayId(int awayId) {
        this.awayId = awayId;
    }

    public String getAwayName() {
        return awayName;
    }

    public void setAwayName(String awayName) {
        this.awayName = awayName;
    }

    public String getAwayShort() {
        return awayShort;
    }

    public void setAwayShort(String awayShort) {
        this.awayShort = awayShort;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public int getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(int isFinished) {
        this.isFinished = isFinished;
    }

    public byte[] getMatchImage() {
        return matchImage;
    }

    public void setMatchImage(byte[] matchImage) {
        this.matchImage = matchImage;
    }

    public MatchDatabase(int matchId, String gameDate, int homeId, String homeName, String homeShort, int homeScore, int awayId, String awayName, String awayShort, int awayScore, int isFinished, byte[] matchImage) {
        this.matchId = matchId;
        this.gameDate = gameDate;
        this.homeId = homeId;
        this.homeName = homeName;
        this.homeShort = homeShort;
        this.homeScore = homeScore;
        this.awayId = awayId;
        this.awayName = awayName;
        this.awayShort = awayShort;
        this.awayScore = awayScore;
        this.isFinished = isFinished;
        this.matchImage = matchImage;
    }
}
