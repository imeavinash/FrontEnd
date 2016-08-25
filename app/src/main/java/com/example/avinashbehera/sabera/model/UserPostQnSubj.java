package com.example.avinashbehera.sabera.model;

import java.util.ArrayList;

/**
 * Created by avinashbehera on 26/08/16.
 */
public class UserPostQnSubj {

    private String questionTxt;

    private String answerTxt;

    private ArrayList<String> keywords;

    private String timer;

    private String hintTxt;

    private String timeStamp;

    public UserPostQnSubj() {
    }

    public UserPostQnSubj(String answerTxt, String hintTxt, ArrayList<String> keywords, String questionTxt, String timer, String timeStamp) {
        this.answerTxt = answerTxt;
        this.hintTxt = hintTxt;
        this.keywords = keywords;
        this.questionTxt = questionTxt;
        this.timer = timer;
        this.timeStamp = timeStamp;
    }

    public String getAnswerTxt() {
        return answerTxt;
    }

    public void setAnswerTxt(String answerTxt) {
        this.answerTxt = answerTxt;
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
