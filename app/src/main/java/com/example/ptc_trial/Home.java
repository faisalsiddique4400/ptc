package com.example.ptc_trial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ptc_trial.Adapters.PostShowAdapter;
import com.example.ptc_trial.Models.PostModel;
import com.example.ptc_trial.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends Fragment implements View.OnClickListener {


    FragmentHomeBinding binding;
    ArrayList<PostModel> postList;
    FirebaseDatabase database;
    PostShowAdapter postAdapter;
    int dogCounter = 0, catCounter = 0, bunnyCounter = 0, birdCounter = 0, otherCounter = 0;
    String totalOf = "Total of ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        postList = new ArrayList<>();
        postAdapter = new PostShowAdapter(getContext(), postList);
        binding.postShowRecyclerView.setAdapter(postAdapter);
        binding.postShowRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        fetchPosts();
        binding.catCategory.setOnClickListener(this);
        binding.dogCategory.setOnClickListener(this);
        binding.birdCategory.setOnClickListener(this);
        binding.bunniesCategory.setOnClickListener(this);
        return binding.getRoot();
    }


    private void populateCategory() {
        String totalOfCat = totalOf + String.valueOf(catCounter);
        String totalOfDog = totalOf + String.valueOf(dogCounter);
        String totalOfBird = totalOf + String.valueOf(otherCounter);
        String totalOfBunny = totalOf + String.valueOf(bunnyCounter);
        binding.catCount.setText(totalOfCat);
        binding.dogCount.setText(totalOfDog);
        binding.bunniesCount.setText(totalOfBunny);
        binding.birdCount.setText(totalOfBird);
    }

    private void fetchPosts() {
        database.getReference("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                if (snapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                            PostModel postModel = snapshot1.getValue(PostModel.class);
                            checkCategory(postModel.getCategory());
                            postList.add(postModel);
                        }
                    }
                    binding.postCardProgressBar.setVisibility(View.INVISIBLE);
                    populateCategory();
                    postAdapter.notifyDataSetChanged();
                } else {
                    binding.postCardProgressBar.setVisibility(View.INVISIBLE);
                    binding.noContent.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkCategory(String category) {
        Log.d("Category", category);

        if (category.equals("Bird"))
            birdCounter += 1;
        else if (category.equals("Bunny"))
            bunnyCounter += 1;
        else if (category.equals("Cat"))
            catCounter += 1;
        else if (category.equals("Dog"))
            dogCounter += 1;
        else
            otherCounter += 1;

    }

    @Override
    public void onClick(View view) {
        // Intent intent = new Intent(getContext(), CategoryDetail.class);
        switch (view.getId()) {
            case R.id.cat_category: {
                Intent intent = new Intent(getContext(), CategoryDetail.class);
                intent.putExtra("Category", "CAT");
                startActivity(intent);
                break;
            }

            case R.id.dog_category: {
                Intent intent = new Intent(getContext(), CategoryDetail.class);
                intent.putExtra("Category", "DOG");
                startActivity(intent);
                break;
            }

            case R.id.bird_category: {
                Intent intent = new Intent(getContext(), CategoryDetail.class);
                intent.putExtra("Category", "BIRD");
                startActivity(intent);
                break;
            }


            case R.id.bunnies_category: {
                Intent intent = new Intent(getContext(), CategoryDetail.class);
                intent.putExtra("Category", "BUNNY");
                startActivity(intent);
                break;
            }

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }
}