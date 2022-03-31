package com.example.ptc_trial.Chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ptc_trial.Adapters.UserAdapter;
import com.example.ptc_trial.Models.User;
import com.example.ptc_trial.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chat extends Fragment {
    FragmentChatBinding binding;
    FirebaseDatabase database;
    ArrayList<User> users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        binding.progressBarChat.setVisibility(View.VISIBLE);
        users = new ArrayList<>();
        UserAdapter userAdapter = new UserAdapter(getContext(), users);
        binding.chatRecyclerView.setAdapter(userAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                if (snapshot.hasChildren()) {
                    binding.progressBarChat.setVisibility(View.INVISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        user.setUserId(dataSnapshot.getKey());
                        if (!user.getUserId().equals(FirebaseAuth.getInstance().getUid())) {
                            users.add(user);
                        }
                    }
                    userAdapter.notifyDataSetChanged();
                }

                if (users.isEmpty()) {
                    binding.noData.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}