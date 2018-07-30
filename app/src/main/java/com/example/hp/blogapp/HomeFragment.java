package com.example.hp.blogapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView blog_list_View;
    //Create list of model class type
    private List<BlogPost> blogList;
    private FirebaseFirestore firebaseFirestore;
    private BlogRecycleAdapter blogRecycleAdapter;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        //Get the recycler View
        blog_list_View = view.findViewById(R.id.blog_list_view);
        blogList = new ArrayList<>();
        blogRecycleAdapter = new BlogRecycleAdapter(blogList);

        //Set adapter to Recycler View
        blog_list_View.setLayoutManager(new LinearLayoutManager(getActivity()));
        blog_list_View.setAdapter(blogRecycleAdapter);



        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                for(DocumentChange doc: documentSnapshots.getDocumentChanges()){

                    if(doc.getType() == DocumentChange.Type.ADDED){

                        //USE MODEL CLASS and save one object obtained into Model class list
                        BlogPost blogPost = doc.getDocument().toObject(BlogPost.class);
                        blogList.add(blogPost);

                        blogRecycleAdapter.notifyDataSetChanged();
                    }
                }

            }
        });



        // Inflate the layout for this fragment
        return view;
    }

}
