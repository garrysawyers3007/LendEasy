package com.lendeasy.lendeasy;

public class ChatModel {
    private String message;
    private String timeStamp;

    public ChatModel(String message, String timeStamp) {
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public ChatModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
