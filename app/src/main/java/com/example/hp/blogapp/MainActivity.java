package com.example.hp.blogapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolBar;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    FloatingActionButton add_post_btn;
    private  String user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        toolBar =  findViewById(R.id.toolbar);
//        setSupportActionBar(toolBar);
//
//        getSupportActionBar().setTitle("Photo Blog");

        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Blog App");

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        add_post_btn = findViewById(R.id.addPostButton);

        add_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser != null){
                    user_id = mAuth.getCurrentUser().getUid();
                    firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult().exists()){
                                Intent addPost = new Intent(MainActivity.this,NewPost.class);
                                startActivity(addPost);
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Please choose profile photo and name", Toast.LENGTH_LONG).show();
                                Intent main = new Intent(MainActivity.this,AccounrSetup.class);
                                startActivity(main);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this, "Please choose profile photo and name", Toast.LENGTH_LONG).show();
                    Intent main = new Intent(MainActivity.this,AccounrSetup.class);
                    startActivity(main);
                }



            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check if user logged in
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            sendLogin();
            finish();
        }

    }

    private void sendLogin() {
       Intent ash = new Intent(MainActivity.this,Login.class);
        startActivity(ash);
    }

    //add menu drawable resource to action bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logout:
                logOut();
                return true;

            case R.id.action_settings:
                Intent acS = new Intent(MainActivity.this,AccounrSetup.class);
                startActivity(acS);

                return true;

                default:
                    return false;
        }
    }

    private void logOut() {
        mAuth.signOut();
        sendLogin();
    }
}
