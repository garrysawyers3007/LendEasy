package com.lendeasy.lendeasy.ChatFunctionality;

public class Contact {
    private String name;
    private String eMail;
    private String userId;

    public Contact(String name, String eMail, String userId) {
        this.name = name;
        this.eMail = eMail;
        this.userId = userId;
    }

    public Contact() {

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return eMail;
    }

    public String getUserId() {
        return userId;
    }
}
