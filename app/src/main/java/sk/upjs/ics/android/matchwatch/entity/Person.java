package sk.upjs.ics.android.matchwatch.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Person implements Serializable {

    private String firstName;

    private String lastName;

    private String[] birthDate;

    private String nationality;

    private String function;

    //constructors
    public Person() {
    }

    public Person(String firstName, String lastName, String[] birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public Person(String firstName, String lastName, String[] birthDate, String nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.birthDate = birthDate;
    }

    // getters & setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String[] getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String[] birthDate) {
        this.birthDate = birthDate;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

}
