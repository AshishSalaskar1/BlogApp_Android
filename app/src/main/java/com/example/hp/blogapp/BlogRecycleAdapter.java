package com.example.hp.blogapp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//THIS IS THE ADAPTER CLASS FOR RECYCLER VIEW
public class BlogRecycleAdapter extends RecyclerView.Adapter<BlogRecycleAdapter.ViewHolder>{


    public List<BlogPost> blogList;
    private Context context;
    private String user_id;
    FirebaseFirestore firebaseFirestore;


    //COnstructor which receives list of model class BlogPost
    public BlogRecycleAdapter(List<BlogPost> blog_list){
            this.blogList = blog_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //INFLATE LAYOUT WITH CREATED LAYOUT ie blost_list_item
        firebaseFirestore =FirebaseFirestore.getInstance();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item,parent,false);
        //forGlide
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        //This gets the data..getDesc is constructor from Model class which fetches the data
        String desc_text = blogList.get(position).getDesc();
        holder.setDescText(desc_text);

        String download_uri = blogList.get(position).getImage_url();
        holder.setImage(download_uri);

        //get user id and retrieve user image stored in Users Collection
        user_id = blogList.get(position).getUser_id();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if (task.isSuccessful()){
                    if(task.getResult().exists()){
                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");

                        // populate both image and username to RwcyclerView

                        holder.setUserImage(image);
                        holder.setUserName(name);

                    }
               }
            }
        });


    }

    //Set This urself
    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView descView;
        private TextView userName;
        private ImageView postImage;
        private CircleImageView userImage;
        private View mview;


        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
        }

        //Set description text
        public void setDescText(String descText){

            descView = mview.findViewById(R.id.blogDescription);
            descView.setText(descText);

        }

        //UserName
        public void setUserName(String userNameText){

            userName = mview.findViewById(R.id.blogUsername);
            userName.setText(userNameText);

        }

        //Set Image
        public void setImage(String downloadURL){
            postImage = mview.findViewById(R.id.blogImage);

            //use glide to save image into ImageView
            Glide.with(context).load(downloadURL).into(postImage);
        }

        //User Image
        public void setUserImage(String userImage_URL){

            userImage = mview.findViewById(R.id.blogProfilePic);
            Glide.with(context).load(userImage_URL).into(userImage);

        }
    }

}
