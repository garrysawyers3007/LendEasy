package com.lendeasy.lendeasy.ChatFunctionality;

import com.google.firebase.Timestamp;

public class ChatModel {
    private String message;
    private Timestamp timeStamp;
    private String senderId;
    private String recieverId;

    public ChatModel() {
    }

    public ChatModel(String message, Timestamp timeStamp, String senderId, String recieverId) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.senderId = senderId;
        this.recieverId = recieverId;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

}

