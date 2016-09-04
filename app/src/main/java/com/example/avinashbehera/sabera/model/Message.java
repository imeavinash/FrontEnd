package com.example.avinashbehera.sabera.model;

/**
 * Created by avinashbehera on 04/09/16.
 */
public class Message {

    private String msgTxt;

    private String userId;

    private String timeStamp;

    public Message() {
    }

    public String getMsgTxt() {
        return msgTxt;
    }

    public void setMsgTxt(String msgTxt) {
        this.msgTxt = msgTxt;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
