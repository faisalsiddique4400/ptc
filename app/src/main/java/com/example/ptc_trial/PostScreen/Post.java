package com.example.ptc_trial.PostScreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ptc_trial.Models.PostModel;
import com.example.ptc_trial.R;
import com.example.ptc_trial.databinding.FragmentPostBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Post extends Fragment implements View.OnClickListener {
    FragmentPostBinding binding;
    int IMAGE_PICK_REQUEST_CODE = 1;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri fileUri = null;
    private PostModel post;
    int counter=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        binding.postButton.setOnClickListener(this);
        post = new PostModel();
        binding.newPost.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.postButton:
                validateDate();
                break;
            case R.id.newPost:
                pickPicture();
        }
    }

    private void validateDate() {
        if (binding.name.getText().toString().isEmpty()) {
            binding.name.setError("Please enter name of the pet");
        } else if (binding.age.getText().toString().isEmpty()) {
            binding.age.setError("Please enter age of the pet");
        } else if (binding.location.getText().toString().isEmpty()) {
            binding.location.setError("Please enter location");
        } else if (binding.description.getText().toString().isEmpty()) {
            binding.description.setError("Please enter description");
        } else if (binding.color.getText().toString().isEmpty()) {
            binding.color.setError("Please enter color of the pet");
        } else if (binding.price.getText().toString().isEmpty()) {
            binding.price.setError("Please enter price");
        } else if (binding.imageView.getTag().equals("Empty")) {
            Toast.makeText(getContext(), "Please add at lease one picture", Toast.LENGTH_SHORT).show();
        } else {
            binding.postScreenProgressBar.setVisibility(View.VISIBLE);
            post.setName(binding.name.getText().toString());
            post.setAge(binding.age.getText().toString());
            post.setLocation(binding.location.getText().toString());
            post.setDescription(binding.description.getText().toString());
            post.setColor(binding.color.getText().toString());
            post.setPrice(binding.price.getText().toString());
            savePost();

        }
    }

    private void savePost() {
        final StorageReference storageRef = storage.getReference().child("posts").child(mAuth.getUid()+counter++);
        storageRef.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        post.setPictureUri(uri.toString());
                        Log.d("Uploaded URI",uri.toString());
                        database.getReference().child("posts").child(mAuth.getUid()).push().setValue(post);
                        binding.postScreenProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                        binding.name.setText("");
                        binding.age.setText("");
                        binding.location.setText("");
                        binding.description.setText("");
                        binding.color.setText("");
                        binding.price.setText("");
                        binding.imageView.setImageURI(Uri.parse(""));
                        binding.imageView.setTag("Empty");
                        fileUri = null;
                    }
                });
            }
        });
    }

    private void pickPicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_REQUEST_CODE) {
            if (data.getData() != null) {
                fileUri = data.getData();
                Log.d("FileURI",fileUri.toString());
                binding.imageView.setImageURI(fileUri);
                binding.imageView.setTag(fileUri);
            }
        }
    }
}