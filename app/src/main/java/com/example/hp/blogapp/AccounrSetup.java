package com.example.hp.blogapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class AccounrSetup extends AppCompatActivity {

    private Toolbar toolbarSetup;
    private ProgressBar progressBar;
    private CircleImageView userImg;
    private Uri main_uri = null;
    private Uri default_uri = null;
    private FirebaseAuth mAuth;
    private boolean isChanged = true;
    private FirebaseFirestore fireStore;
    private EditText Name;
    private StorageReference mStorageRef;
    Button Submit;
    private String user_id;
    private Bitmap compressedImageBitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounr_setup);

        //Toolbar
        toolbarSetup = findViewById(R.id.toolbarSetup);
        setSupportActionBar(toolbarSetup);
        getSupportActionBar().setTitle("Account Setup");

         Submit = findViewById(R.id.submit);
        progressBar = findViewById(R.id.AccountSettingsBar);
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        fireStore = FirebaseFirestore.getInstance();
        Name = findViewById(R.id.name);
        userImg = findViewById(R.id.profile);
        default_uri = Uri.parse("R.mipmap.user");

        //Evertime settings load check if data is already present in FireStore, if yes retrive and set name and image using glide
        fireStore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        isChanged = false;
                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");
//                        Toast.makeText(AccounrSetup.this,"DATA EXISTS",Toast.LENGTH_LONG).show();
                        Name.setText(name);

                        //GLIDE APP set default background
                        RequestOptions placeHolder = new RequestOptions();
                        placeHolder.placeholder(R.mipmap.user);

                        //Convert image string to URI and store it in mainImageUri
                        main_uri = Uri.parse(image);

                        Glide.with(AccounrSetup.this).setDefaultRequestOptions( placeHolder.placeholder(R.mipmap.user)).load(image).into(userImg);

                    }
                    else{
                        main_uri = default_uri;
                        Toast.makeText(AccounrSetup.this,"NO DATA EXISTS",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(AccounrSetup.this,"Firestore Retrieve Error OUTSIDE",Toast.LENGTH_LONG).show();
                }
            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String uName = Name.getText().toString();
                if (isChanged) {
                    if (!TextUtils.isEmpty(uName) && main_uri != null) {
                        progressBar.setVisibility(View.VISIBLE);
//                    String user_id = mAuth.getCurrentUser().getUid();

                        File imageFile = new File(main_uri.getPath());

                        try {
                            compressedImageBitmap = new Compressor(AccounrSetup.this)
                                    .setMaxHeight(100)
                                    .setMaxWidth(100)
                                    .setQuality(10)
                                    .compressToBitmap(imageFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Uploadinf bitmap to firebase
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] thumbBitmap = baos.toByteArray();

                        UploadTask thumbImage = mStorageRef.child("/Profile_Photos/thumbs").child(user_id+".jpg").putBytes(thumbBitmap);
                        thumbImage.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    saveToFirestore(task, uName);
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(AccounrSetup.this, " Image Error" + error, Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        });
//
                    }
                    else {
                        Toast.makeText(AccounrSetup.this, "No image", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                else {
                    Toast.makeText(AccounrSetup.this, "Image not changed", Toast.LENGTH_LONG).show();
                    saveToFirestore(null,Name.getText().toString());
                }
            }

        });

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //REad permisssion available in less tham Marhmallow..else include code to get the permsion

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    Toast.makeText(AccounrSetup.this, "ReadPerm", Toast.LENGTH_LONG).show();
                    if (ContextCompat.checkSelfPermission(AccounrSetup.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AccounrSetup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                        ActivityCompat.requestPermissions(AccounrSetup.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                    }
                    Toast.makeText(AccounrSetup.this, "WritePerm", Toast.LENGTH_LONG).show();
                    if (ContextCompat.checkSelfPermission(AccounrSetup.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(AccounrSetup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                        ActivityCompat.requestPermissions(AccounrSetup.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

                    }
                }
                getPicture();
            }

            private void getPicture() {
                Toast.makeText(AccounrSetup.this,"Successfully Saved",Toast.LENGTH_LONG).show();
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(AccounrSetup.this);
            }


        });
    }

//    private void saveToFirestore(@NonNull Task<UploadTask.TaskSnapshot> task,String uName) {
    private void saveToFirestore(Task<UploadTask.TaskSnapshot> task, final String uName) {

         Uri downloadUri;
        //If task is not null that is change occured. So get new URI for image also change ThumbNail
        if(task != null) {
             downloadUri = task.getResult().getDownloadUrl();

//
        }

        else {
             downloadUri = main_uri;
        }
        //Create map..with keys common and values
        Map<String,String> userMap= new HashMap<>();

        userMap.put("name",uName);
        userMap.put("image",downloadUri.toString());
        fireStore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AccounrSetup.this, "Settings Saved Succesfully", Toast.LENGTH_LONG).show();
                    Intent main = new Intent(AccounrSetup.this,MainActivity.class);
                    startActivity(main);
                }
                else {
                    String error = task.getException().getMessage();
                    Toast.makeText(AccounrSetup.this, " FireStore Error" + error, Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                main_uri = result.getUri();
                userImg.setImageURI(main_uri);
                isChanged = true;
                String tct = main_uri.toString();
//                Toast.makeText(AccounrSetup.this,tct,Toast.LENGTH_LONG).show();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

            }
        }
    }


}




