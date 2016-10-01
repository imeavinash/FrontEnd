package com.avinashbehera.sabera.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by avinashbehera on 02/09/16.
 */
public class UserSeeQn implements Serializable {

    private String qnText;

    private String qnType;

    private String qId;

    private String uId;

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    private String ansText;

    private ArrayList<String> keywords;

    private String option1;

    private String option2;

    private String option3;

    private String option4;

    private boolean status1;

    private boolean status2;

    private boolean status3;

    private boolean status4;

    private String timer;

    private String hintText;

    public UserSeeQn() {
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getAnsText() {
        return ansText;
    }

    public void setAnsText(String ansText) {
        this.ansText = ansText;
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public String getQnText() {
        return qnText;
    }

    public void setQnText(String qnText) {
        this.qnText = qnText;
    }

    public String getQnType() {
        return qnType;
    }

    public void setQnType(String qnType) {
        this.qnType = qnType;
    }

    public boolean isStatus1() {
        return status1;
    }

    public void setStatus1(boolean status1) {
        this.status1 = status1;
    }

    public boolean isStatus2() {
        return status2;
    }

    public void setStatus2(boolean status2) {
        this.status2 = status2;
    }

    public boolean isStatus3() {
        return status3;
    }

    public void setStatus3(boolean status3) {
        this.status3 = status3;
    }

    public boolean isStatus4() {
        return status4;
    }

    public void setStatus4(boolean status4) {
        this.status4 = status4;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }
}
