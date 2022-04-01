package com.example.ptc_trial.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ptc_trial.IndividualPostShowPage;
import com.example.ptc_trial.Models.PostModel;
import com.example.ptc_trial.R;
import com.google.android.material.card.MaterialCardView;
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
        Picasso.get().load(post.getPictureUri()).placeholder(null).into(holder.imageView);
        holder.name.setText(post.getName());
        holder.location.setText(post.getLocation());
        holder.price.setText(post.getPrice());
        holder.description.setText(post.getDescription());
        holder.dataCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, IndividualPostShowPage.class);
                intent.putExtra("PostData", postList.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, description, location, price;
        MaterialCardView dataCard;
        ProgressBar postCardProgressBar;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            dataCard = itemView.findViewById(R.id.DataCard);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            location = itemView.findViewById(R.id.location);
            price = itemView.findViewById(R.id.price);
        }
    }

}
