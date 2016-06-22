package sk.upjs.ics.android.matchwatch.dbsEntity;

import java.io.Serializable;

public class PenaltyDatabase implements Serializable {

    private int matchId;

    private int penaltyId;

    private int playerId;

    private String time;

    private String period;

    private String duration;

    private int teamId;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getPenaltyId() {
        return penaltyId;
    }

    public void setPenaltyId(int penaltyId) {
        this.penaltyId = penaltyId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public PenaltyDatabase(int matchId, int penaltyId, int playerId, String duration, String time, int teamId, String period) {
        this.matchId = matchId;
        this.penaltyId = penaltyId;
        this.playerId = playerId;
        this.duration = duration;
        this.time = time;
        this.teamId = teamId;
        this.period = period;
    }
}
