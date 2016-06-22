package sk.upjs.ics.android.matchwatch.dbsEntity;

import java.io.Serializable;

public class InterruptionDatabase implements Serializable {

    private int matchId;

    private int interruptionId;

    private String time;

    private String period;

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

    public int getInterruptionId() {
        return interruptionId;
    }

    public void setInterruptionId(int interruptionId) {
        this.interruptionId = interruptionId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public InterruptionDatabase(int matchId, int interruptionId, String time, int teamId, String period) {
        this.matchId = matchId;
        this.interruptionId = interruptionId;
        this.time = time;
        this.teamId = teamId;
        this.period = period;
    }
}
