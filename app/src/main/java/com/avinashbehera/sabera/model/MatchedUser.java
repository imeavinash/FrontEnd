package com.avinashbehera.sabera.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by avinashbehera on 04/09/16.
 */
public class MatchedUser implements Serializable{

    private String userId;
    private String email;
    private String name;
    private String dob;
    private String gender;
    private String categories;
    private String encodedImage;
    private ArrayList<MatchedUserQn> qnsAnswered;
    private ArrayList<MatchedUserQn> qnsAsked;
    private ArrayList<Message> messagesList;

    public MatchedUser() {
    }

    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public ArrayList<Message> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(ArrayList<Message> messagesList) {
        this.messagesList = messagesList;
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
