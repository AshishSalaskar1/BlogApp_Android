package com.example.hp.blogapp;

//import java.sql.Timestamp;


//THIS IS THE MODEL CLASS
public class BlogPost {

    public String image_url, image_thumb, desc,user_id;


    public BlogPost(){

    }

    public BlogPost(String image_url, String image_thumb, String desc, String user_id) {
        this.image_url = image_url;
        this.image_thumb = image_thumb;
        this.desc = desc;
        this.user_id = user_id;
//        this.timeStamp = timeStamp;
    }

    public String getImage_url() {

        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

//    public Timestamp getTimeStamp() {
//        return timeStamp;
//    }
//
//    public void setTimeStamp(Timestamp timeStamp) {
//        this.timeStamp = timeStamp;
//    }
}
