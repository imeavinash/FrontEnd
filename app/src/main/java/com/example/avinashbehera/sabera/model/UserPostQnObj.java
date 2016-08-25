package com.example.avinashbehera.sabera.model;

import java.util.ArrayList;

/**
 * Created by avinashbehera on 26/08/16.
 */
public class UserPostQnObj {

    private String questionTxt;

    private ArrayList<String> options;

    private ArrayList<Integer> correctOptions;

    private String timer;

    private String hintTxt;

    private String timeStamp;

    public UserPostQnObj() {
    }

    public UserPostQnObj(ArrayList<Integer> correctOptions, String hintTxt, ArrayList<String> options, String questionTxt, String timer, String timeStamp) {
        this.correctOptions = correctOptions;
        this.hintTxt = hintTxt;
        this.options = options;
        this.questionTxt = questionTxt;
        this.timer = timer;
        this.timeStamp = timeStamp;
    }

    public ArrayList<Integer> getCorrectOptions() {
        return correctOptions;
    }

    public void setCorrectOptions(ArrayList<Integer> correctOptions) {
        this.correctOptions = correctOptions;
    }

    public String getHintTxt() {
        return hintTxt;
    }

    public void setHintTxt(String hintTxt) {
        this.hintTxt = hintTxt;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getQuestionTxt() {
        return questionTxt;
    }

    public void setQuestionTxt(String questionTxt) {
        this.questionTxt = questionTxt;
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
