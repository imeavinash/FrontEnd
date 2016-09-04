package com.example.avinashbehera.sabera.model;

import android.widget.LinearLayout;

import org.json.simple.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by avinashbehera on 20/08/16.
 */
public class User implements Serializable{

    public User() {
    }

    private String pwd;

    private String gcmRegToken;

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

    private ArrayList<UserSeeQn> questionArray;

    private JSONArray qnJsonArray;

    private String Questions;

    private ArrayList<MatchedUser> matchedUserList;

    private HashMap<String,MatchedUser> matchedUserHashMap;

    private HashMap<String,ArrayList<Message>> matchedUserMessageMap;

    private ArrayList<MsgFrgmChatHead> chatHeadsArrayList;

    public ArrayList<MsgFrgmChatHead> getChatHeadsArrayList() {
        return chatHeadsArrayList;
    }

    public void setChatHeadsArrayList(ArrayList<MsgFrgmChatHead> chatHeadsArrayList) {
        this.chatHeadsArrayList = chatHeadsArrayList;
    }

    public HashMap<String, ArrayList<Message>> getMatchedUserMessageMap() {
        return matchedUserMessageMap;
    }

    public void setMatchedUserMessageMap(HashMap<String, ArrayList<Message>> matchedUserMessageMap) {
        this.matchedUserMessageMap = matchedUserMessageMap;
    }

    public HashMap<String, MatchedUser> getMatchedUserHashMap() {
        return matchedUserHashMap;
    }

    public void setMatchedUserHashMap(HashMap<String, MatchedUser> matchedUserHashMap) {
        this.matchedUserHashMap = matchedUserHashMap;
    }

    public ArrayList<MatchedUser> getMatchedUserList() {
        return matchedUserList;
    }

    public void setMatchedUserList(ArrayList<MatchedUser> matchedUserList) {
        this.matchedUserList = matchedUserList;
    }

    public String getGcmRegToken() {
        return gcmRegToken;
    }

    public void setGcmRegToken(String gcmRegToken) {
        this.gcmRegToken = gcmRegToken;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }



    public JSONArray getQnJsonArray() {
        return qnJsonArray;
    }

    public void setQnJsonArray(JSONArray qnJsonArray) {
        this.qnJsonArray = qnJsonArray;
    }

    public ArrayList<UserSeeQn> getQuestionArray() {
        return questionArray;
    }

    public void setQuestionArray(ArrayList<UserSeeQn> questionArray) {
        this.questionArray = questionArray;
    }



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
