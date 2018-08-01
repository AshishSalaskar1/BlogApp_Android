package com.example.hp.blogapp;

//import java.sql.Timestamp;


import java.util.Date;

//THIS IS THE MODEL CLASS
public class BlogPost extends BlogPostId {

    public String image_url, image_thumb, desc,user_id;
    public Date timeStamp;



    //MUST ADD NEEDED
    public BlogPost(){

    }

    public BlogPost(String image_url, String image_thumb, String desc, String user_id,Date timeStamp) {
        this.image_url = image_url;
        this.image_thumb = image_thumb;
        this.desc = desc;
        this.user_id = user_id;
       this.timeStamp = timeStamp;
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

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

}
