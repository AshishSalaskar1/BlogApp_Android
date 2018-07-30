package com.example.hp.blogapp;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

//THIS IS THE ADAPTER CLASS FOR RECYCLER VIEW
public class BlogRecycleAdapter extends RecyclerView.Adapter<BlogRecycleAdapter.ViewHolder>{


    public List<BlogPost> blogList;
    //COnstructor which receives list of model class BlogPost
    public BlogRecycleAdapter(List<BlogPost> blog_list){
            this.blogList = blog_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //INFLATE LAYOUT WITH CREATED LAYOUT
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //This gets the data..getDesc is constructor from Model class which fetches the data
        String desc_text = blogList.get(position).getDesc();
        //Call setDescText Function
        holder.setDescText(desc_text);

    }

    //Set This urself
    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView descView;
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
    }

}
