package com.example.hp.blogapp;


import com.google.firebase.firestore.Exclude;

import io.reactivex.annotations.NonNull;

//Extender class
public class BlogPostId {

    @Exclude
    public String BlogPostId;

    public <T extends BlogPostId> T withId(@NonNull final String id){
        this.BlogPostId = id;
        return (T) this;
    }

}
