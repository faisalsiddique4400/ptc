package com.example.ptc_trial.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ptc_trial.Models.PostModel;
import com.example.ptc_trial.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostShowAdapter extends RecyclerView.Adapter<PostShowAdapter.PostViewHolder> {

    Context context;
    ArrayList<PostModel> postList;

    public PostShowAdapter(Context context, ArrayList<PostModel> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_show, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostModel post = postList.get(position);
        Picasso.get().load(post.getPictureUri()).placeholder(R.drawable.avatar).into(holder.imageView);
        holder.name.setText(post.getName());
        holder.location.setText(post.getLocation());
        holder.price.setText(post.getPrice());
        holder.description.setText(post.getDescription());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, description, location, price;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            location = itemView.findViewById(R.id.location);
            price = itemView.findViewById(R.id.price);
        }
    }

}
