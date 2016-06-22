package sk.upjs.ics.android.matchwatch.entity;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Arrays;

public class Player extends Person implements Serializable {

    private int number;

    private String position;

    private char shoots;

    private double height;

    private int weight;

    private String club;

    // private StatsPlayer[] statistics;


    // constructors
    public Player() {
    }

    public Player(String firstName, String lastName, String[] birthdDate, String position, int number, char shoots, double height, int weight, String club) {
        super(firstName, lastName, birthdDate);
        this.position = position;
        this.number = number;
        this.shoots = shoots;
        this.height = height;
        this.weight = weight;
        this.club = club;
    }

    // getters & setters
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public char getShoots() {
        return shoots;
    }

    public void setShoots(char shoots) {
        this.shoots = shoots;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    /* public StatsPlayer[] getStatistics() {
        return statistics;
    }

    public void setStatistics(StatsPlayer[] statistics) {
        this.statistics = statistics;
    }*/

    public String toString() {
        return "; First Name: " + getFirstName() +
                "; Last Name: " + getLastName() +
                "; Birth date: " + Arrays.toString(getBirthDate()) +
                "; Nationality: " + getNationality() +
                "; Position: " + getPosition() +
                "; Number: " + getNumber() +
                "; Shoots: " + getShoots() +
                "; Height: " + getHeight() +
                "; Weight: " + getWeight() +
                "; Club: " + getClub();
    }

}
