package com.example.hp.blogapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Comments extends AppCompatActivity {

    private Toolbar toolbarNewPost;
    private String BlogPostId;
    private EditText commentsText;
    private ImageView commentSubmit;
    private RecyclerView comment_list_view;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    public List<CommentModelClass> commentList;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        BlogPostId = getIntent().getStringExtra("BlogPostId");


        toolbarNewPost = findViewById(R.id.toolbarComments);
        setSupportActionBar(toolbarNewPost);
        getSupportActionBar().setTitle("Comments");
        //add back button to parent activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        commentsText = findViewById(R.id.comment_text);
        commentSubmit = findViewById(R.id.commentSubmit);

        commentsText.requestFocus();



        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Get the recycler View
        comment_list_view = findViewById(R.id.recycler_view_comments);
        commentList = new ArrayList<>();
        commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentList);

        //Set adapter to Recycler View
        comment_list_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        comment_list_view.setAdapter(commentsRecyclerAdapter);

//        Retrieve Comments into Recycler view
        Query mainQuery = firebaseFirestore.collection("Posts/" + BlogPostId + "/Comments")
                .orderBy("timeStamp", Query.Direction.ASCENDING);

        mainQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            //USE MODEL CLASS and save one object obtained into Model class list
                            CommentModelClass commentData = doc.getDocument().toObject(CommentModelClass.class);
                            commentList.add(commentData);
                            commentsRecyclerAdapter.notifyDataSetChanged();
                        }
                    }


            }
        });




        //Read comments
        commentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String comment = commentsText.getText().toString();

                if(!comment.isEmpty()){

                    Map<String,Object> commentMap = new HashMap<>();
                    commentMap.put("Message",comment);
                    commentMap.put("user_id",mAuth.getCurrentUser().getUid());
                    commentMap.put("timeStamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("Posts/" + BlogPostId + "/Comments").add(commentMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                            if(task.isSuccessful()){
                                commentsText.setText(null);
                            }

                        }
                    });

                }
            }
        });

    }
}
