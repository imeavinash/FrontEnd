package com.example.avinashbehera.sabera.model;

import java.io.Serializable;

/**
 * Created by avinashbehera on 04/09/16.
 */
public class MsgFrgmChatHead implements Serializable {

    private String userId;
    private String name;
    private String lastMessage;
    private String timeStamp;
    private int unreadMsgCount;

    public MsgFrgmChatHead() {
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getUnreadMsgCount() {
        return unreadMsgCount;
    }

    public void setUnreadMsgCount(int unreadMsgCount) {
        this.unreadMsgCount = unreadMsgCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
