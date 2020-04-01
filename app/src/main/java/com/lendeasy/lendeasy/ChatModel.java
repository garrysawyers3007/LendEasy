package com.lendeasy.lendeasy;

import com.google.firebase.Timestamp;

public class ChatModel {
    private String message;
    private Timestamp timeStamp;

    public ChatModel(String message, Timestamp timeStamp) {
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public ChatModel() {
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }
}

