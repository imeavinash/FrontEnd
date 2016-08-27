package com.example.avinashbehera.sabera.model;

import java.util.ArrayList;

/**
 * Created by avinashbehera on 26/08/16.
 */
public class UserPostQn {

    private String qnTxt;

    private String qnType;

    private String ansTxt;

    private ArrayList<String> keywords;

    private ArrayList<String> options;

    private ArrayList<Integer> correctOptns;

    private String timer;

    private String hintTxt;

    private String timeStamp;

    private ArrayList<String> categories;

    public UserPostQn() {
    }

    // Constructor for Subjective Qn

    public UserPostQn(String anTxt, String hintTxt, ArrayList<String> keywords, String qnTxt, String qnType, String timer, String timeStamp,ArrayList<String> categories) {
        this.ansTxt = anTxt;
        this.hintTxt = hintTxt;
        this.keywords = keywords;
        this.qnTxt = qnTxt;
        this.qnType = qnType;
        this.timer = timer;
        this.timeStamp = timeStamp;
        this.categories = categories;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
    //Constructor for Objective Qn

    public UserPostQn(ArrayList<Integer> correctOptns, String hintTxt, ArrayList<String> options, String qnTxt, String qnType, String timer, String timeStamp,ArrayList<String> categories) {
        this.correctOptns = correctOptns;
        this.hintTxt = hintTxt;
        this.options = options;
        this.qnTxt = qnTxt;
        this.qnType = qnType;
        this.timer = timer;
        this.timeStamp = timeStamp;
        this.categories = categories;
    }

    public String getAnsTxt() {
        return ansTxt;
    }

    public void setAnsTxt(String anTxt) {
        this.ansTxt = anTxt;
    }

    public ArrayList<Integer> getCorrectOptns() {
        return correctOptns;
    }

    public void setCorrectOptns(ArrayList<Integer> correctOptns) {
        this.correctOptns = correctOptns;
    }

    public String getHintTxt() {
        return hintTxt;
    }

    public void setHintTxt(String hintTxt) {
        this.hintTxt = hintTxt;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getQnTxt() {
        return qnTxt;
    }

    public void setQnTxt(String qnTxt) {
        this.qnTxt = qnTxt;
    }

    public String getQnType() {
        return qnType;
    }

    public void setQnType(String qnType) {
        this.qnType = qnType;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
