package sk.upjs.ics.android.matchwatch.entity;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

public class Team implements Serializable {

    private String fullName;

    private String shortName;

    private List<Player> players;

    // constructors
    public Team() {
    }

    public Team(String fullName, String shortName) {
        this.fullName = fullName;
        this.shortName = shortName;
    }

    public Team(String fullName, String shortName, List<Player> players) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.players = players;
    }

    // methods
    public String toString() {
        return "[" + getFullName() +
                " (" + getShortName() + ")" +
                ", Players: " + getPlayers();
    }

    // getters & setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
