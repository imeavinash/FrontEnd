package com.avinashbehera.sabera.model;

import java.io.Serializable;

/**
 * Created by avinashbehera on 04/09/16.
 */
public class Message implements Serializable {

    private String messageId;

    private String msgTxt;

    private String senderId;

    private String recvrId;

    private String timeStamp;

    public Message() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

    public String getRecvrId() {
        return recvrId;
    }

    public void setRecvrId(String recvrId) {
        this.recvrId = recvrId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
