package com.example.hp.blogapp;

import java.util.Date;

public class CommentModelClass {

    private String Message, user_id;

    public CommentModelClass(){

    }

    public CommentModelClass(String message, String user_id, Date timeStamp) {
        Message = message;
        this.user_id = user_id;
        this.timeStamp = timeStamp;
    }

    private Date timeStamp;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
