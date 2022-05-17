package com.example.ptc_trial.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ptc_trial.Adapters.ChatAdapter;
import com.example.ptc_trial.MainActivity;
import com.example.ptc_trial.Models.Message;
import com.example.ptc_trial.R;
import com.example.ptc_trial.databinding.ActivityChatScreenBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatScreen extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseAuth auth;
    ActivityChatScreenBinding binding;
    Context context;
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra("ChatScreen",2);
//        finish();
//        startActivity(intent);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatScreenBinding.inflate(getLayoutInflater());
        context=this;
        setContentView(binding.getRoot());
        binding.chatScreenProgressBar.setVisibility(View.VISIBLE);
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        final String senderId = auth.getUid();
        final String receiverId = intent.getStringExtra("UserID");
        final String userName = intent.getStringExtra("ProfileName");
        final String profilePic = intent.getStringExtra("ProfilePic");
        binding.chatDisplayName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.chatAvatar);

        binding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("ChatScreen",2);
                finish();
                startActivity(intent);
            }
        });


        ArrayList<Message> messages = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(this, messages, receiverId);

        binding.sendScreenRecyclerView.setAdapter(chatAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.sendScreenRecyclerView.setLayoutManager(manager);

        final String senderRoom = senderId + receiverId;
        final String receiverRoom = receiverId + senderId;


        database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.chatScreenProgressBar.setVisibility(View.INVISIBLE);
                messages.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Message messageModel = snapshot1.getValue(Message.class);
                    messageModel.setMessageId(snapshot1.getKey());
                    messages.add(messageModel);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.sendText.getText().toString();
                final Message messageModel = new Message(senderId, message);
                messageModel.setTime(new Date().getTime());
                binding.sendText.setText("");
                database.getReference().child("chats").child(senderRoom).push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats").child(receiverRoom).push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });


    }
}