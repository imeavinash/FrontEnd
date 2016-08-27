package com.example.avinashbehera.sabera.model;

import java.util.ArrayList;

/**
 * Created by avinashbehera on 20/08/16.
 */
public class User {

    public User() {
    }

    private String pwd;



    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    private String name;

    private String email;

    private String facebookID;

    private String firstName;

    private String lastName;

    private String gender;

    private String birthday;

    private String saberaId;

    private ArrayList<String> categoryList;

    private String categories;

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    private String Questions;

    public ArrayList<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<String> categoryList) {
        this.categoryList = categoryList;
    }

    public String getPwd() {
        return pwd;
    }

    public String getQuestions() {
        return Questions;
    }

    public void setQuestions(String questions) {
        Questions = questions;
    }

    public String getSaberaId() {
        return saberaId;
    }

    public void setSaberaId(String saberaId) {
        this.saberaId = saberaId;
    }

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







    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public User(String name, String email, String facebookID, String gender, String birthday) {
        this.name = name;
        this.email = email;
        this.facebookID = facebookID;
        this.gender = gender;
        this.birthday = birthday;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
