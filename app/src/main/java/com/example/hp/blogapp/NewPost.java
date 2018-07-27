package com.example.hp.blogapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class NewPost extends AppCompatActivity {
    Toolbar toolbarNewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        //Toolbar
        toolbarNewPost = findViewById(R.id.toolbarNewPost);
        setSupportActionBar(toolbarNewPost);
        getSupportActionBar().setTitle("Add New Post");


    }


}
