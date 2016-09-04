package com.example.avinashbehera.sabera.model;

import java.util.ArrayList;

/**
 * Created by avinashbehera on 04/09/16.
 */
public class MatchedUser {

    private String userId;
    private String email;
    private String name;
    private String dob;
    private String gender;
    private ArrayList<MatchedUserQn> qnsAnswered;
    private ArrayList<MatchedUserQn> qnsAsked;

    public MatchedUser() {
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<MatchedUserQn> getQnsAnswered() {
        return qnsAnswered;
    }

    public void setQnsAnswered(ArrayList<MatchedUserQn> qnsAnswered) {
        this.qnsAnswered = qnsAnswered;
    }

    public ArrayList<MatchedUserQn> getQnsAsked() {
        return qnsAsked;
    }

    public void setQnsAsked(ArrayList<MatchedUserQn> qnsAsked) {
        this.qnsAsked = qnsAsked;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
