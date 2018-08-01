package com.example.hp.blogapp;

public class Users {

    private String name,image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Users(String name, String image) {

        this.name = name;
        this.image = image;
    }
}
