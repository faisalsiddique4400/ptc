package com.example.ptc_trial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ptc_trial.Adapters.CategoryAdapter;
import com.example.ptc_trial.Models.PostModel;
import com.example.ptc_trial.databinding.ActivityCategoryDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryDetail extends AppCompatActivity {

    ActivityCategoryDetailBinding binding;
    Intent intent;
    FirebaseDatabase database;
    ArrayList<PostModel> categoryList;
    String category;
    CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent = getIntent();
        database = FirebaseDatabase.getInstance();
        categoryList = new ArrayList<>();
        if (intent != null) {
            category = intent.getStringExtra("Category");
        }
        categoryAdapter = new CategoryAdapter(this, categoryList);
        binding.detailRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.detailRecView.setAdapter(categoryAdapter);
        populateList();
    }

    private void populateList() {
        database.getReference("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                if (snapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                            PostModel postModel = snapshot1.getValue(PostModel.class);
                            if (postModel.getCategory().toLowerCase().equals(category.toLowerCase()))
                                categoryList.add(postModel);
                        }
                    }
                    binding.categoryProgressBar.setVisibility(View.INVISIBLE);
                    categoryAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}