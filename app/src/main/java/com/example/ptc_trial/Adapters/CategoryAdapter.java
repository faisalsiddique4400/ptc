package com.example.ptc_trial.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ptc_trial.IndividualPostShowPage;
import com.example.ptc_trial.Models.PostModel;
import com.example.ptc_trial.R;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<PostModel> categoryList;

    public CategoryAdapter(Context context, ArrayList<PostModel> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_detail_view, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        PostModel post = categoryList.get(position);
        holder.petName.setText(post.getName());
        holder.petLocation.setText(post.getLocation());
        holder.petDescription.setText(post.getDescription());
        holder.petCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, IndividualPostShowPage.class);
                intent.putExtra("PostData", categoryList.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("LISTSIZE",String.valueOf(categoryList.size()));
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView petName;
        TextView petLocation;
        TextView petDescription;
        MaterialCardView petCard;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            petName = itemView.findViewById(R.id.pet_name);
            petLocation = itemView.findViewById(R.id.pet_location);
            petDescription = itemView.findViewById(R.id.pet_description);
            petCard = itemView.findViewById(R.id.pet_card);
        }
    }
}
