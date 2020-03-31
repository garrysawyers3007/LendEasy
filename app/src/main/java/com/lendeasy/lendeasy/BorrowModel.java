package com.lendeasy.lendeasy;

public class BorrowModel {

    private String Name,Title,Description,ImageUrl,Time;

    public BorrowModel(String name, String title, String description, String imageUrl,String time) {
        Name = name;
        Title = title;
        Description = description;
        ImageUrl = imageUrl;
        Time=time;
    }

    public String getName() {
        return Name;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getTime() {
        return Time;
    }
}
