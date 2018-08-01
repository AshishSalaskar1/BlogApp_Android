package com.example.hp.blogapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    private ProgressBar progressReg;
    private EditText emailR,passR,passCon;
    private Button registerB;
    private TextView loggedIn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStore;
    private String user_id;
    private Uri main_uri;
    private Uri upload_uri;
    private StorageReference mStorageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailR = findViewById(R.id.emailReg);
        passR = findViewById(R.id.passReg);
        passCon = findViewById(R.id.passConReg);
        registerB =findViewById(R.id.regB);
        loggedIn = findViewById(R.id.loggedIn);
        progressReg = findViewById(R.id.progressReg);

        mAuth = FirebaseAuth.getInstance();


        loggedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToLogin();
                finish();
            }
        });

        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail,pass,passC;
                mail = emailR.getText().toString().trim();
                pass = passR.getText().toString().trim();
                passC = passCon.getText().toString().trim();


                if(!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(passC)){
                    if(pass.equals(passC)){
                        progressReg.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(register.this,"Welcome",Toast.LENGTH_LONG).show();
                                    Intent acS = new Intent(register.this,AccounrSetup.class);
                                    startActivity(acS);
                                    finish();
                                }
                                else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(register.this,"Error:"+error,Toast.LENGTH_LONG).show();
                                }
                                progressReg.setVisibility(View.INVISIBLE);
                            }
                        });

                    }
                    else{
                        Toast.makeText(register.this,"Passwords don't match",Toast.LENGTH_LONG).show();

                    }
                }

            }


        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cur = mAuth.getCurrentUser();
        if(cur != null){
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent main = new Intent(register.this,MainActivity.class);
        startActivity(main);
    }

    private void sendToLogin(){
        Intent log = new Intent(register.this,Login.class);
        startActivity(log);
    }
}
