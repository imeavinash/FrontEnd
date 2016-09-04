package com.example.avinashbehera.sabera.model;

import java.util.ArrayList;

/**
 * Created by avinashbehera on 04/09/16.
 */
public class MatchedUserQn {

    private String qId;

    private String qnTxt;

    private String qnType;

    private String proposed_answer;

    private String given_answer;

    private ArrayList<String> options;

    private ArrayList<String> correctOptions;

    public MatchedUserQn() {
    }

    public ArrayList<String> getCorrectOptions() {
        return correctOptions;
    }

    public void setCorrectOptions(ArrayList<String> correctOptions) {
        this.correctOptions = correctOptions;
    }

    public String getGiven_answer() {
        return given_answer;
    }

    public void setGiven_answer(String given_answer) {
        this.given_answer = given_answer;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getProposed_answer() {
        return proposed_answer;
    }

    public void setProposed_answer(String proposed_answer) {
        this.proposed_answer = proposed_answer;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
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
}
