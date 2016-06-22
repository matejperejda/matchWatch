package sk.upjs.ics.android.matchwatch.dbsEntity;

import java.io.Serializable;

public class GoalDatabase implements Serializable {

    private int matchId;

    private int playerId;

    private String time;

    private String period;

    private String goalType;

    private int assistIdSt;

    private int assistIdNd;

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

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
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

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public int getAssistIdSt() {
        return assistIdSt;
    }

    public void setAssistIdSt(int assistIdSt) {
        this.assistIdSt = assistIdSt;
    }

    public int getAssistIdNd() {
        return assistIdNd;
    }

    public void setAssistIdNd(int assistIdNd) {
        this.assistIdNd = assistIdNd;
    }

    public GoalDatabase(int matchId, int playerId, String time, String goalType, int assistIdSt, int assistIdNd, int teamId, String period) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.time = time;
        this.goalType = goalType;
        this.assistIdSt = assistIdSt;
        this.assistIdNd = assistIdNd;
        this.teamId = teamId;
        this.period = period;
    }
}
