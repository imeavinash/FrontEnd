package com.avinashbehera.sabera.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by avinashbehera on 04/09/16.
 */
public class MatchedUserQn implements Serializable {

    private String qnTxt;

    private String qnrId;

    private String answererId;

    private ArrayList<String> proposed_answer;

    private ArrayList<String> attempted_answer;

    public MatchedUserQn() {
    }

    public String getAnswererId() {
        return answererId;
    }

    public void setAnswererId(String answererId) {
        this.answererId = answererId;
    }

    public String getQnrId() {
        return qnrId;
    }

    public void setQnrId(String qnrId) {
        this.qnrId = qnrId;
    }

    public ArrayList<String> getAttempted_answer() {
        return attempted_answer;
    }

    public void setAttempted_answer(ArrayList<String> attempted_answer) {
        this.attempted_answer = attempted_answer;
    }

    public ArrayList<String> getProposed_answer() {
        return proposed_answer;
    }

    public void setProposed_answer(ArrayList<String> proposed_answer) {
        this.proposed_answer = proposed_answer;
    }

    public String getQnTxt() {
        return qnTxt;
    }

    public void setQnTxt(String qnTxt) {
        this.qnTxt = qnTxt;
    }

}
