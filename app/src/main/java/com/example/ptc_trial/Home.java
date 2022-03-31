package com.example.ptc_trial;

import android.os.Bundle;
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

public class Home extends Fragment {


    FragmentHomeBinding binding;
    ArrayList<PostModel> postList;
    FirebaseDatabase database;
    PostShowAdapter postAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        postList = new ArrayList<>();
        postAdapter = new PostShowAdapter(getContext(), postList);
        binding.postShowRecyclerView.setAdapter(postAdapter);
        binding.postShowRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        fetPosts();

        return binding.getRoot();
    }

    private void fetPosts() {
        database.getReference("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                if (snapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                            PostModel postModel = snapshot1.getValue(PostModel.class);
                            postList.add(postModel);
                        }
                    }
                    postAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}