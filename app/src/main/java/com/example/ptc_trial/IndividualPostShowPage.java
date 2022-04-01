package com.example.ptc_trial;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ptc_trial.Models.PostModel;
import com.example.ptc_trial.databinding.ActivityIndividualPostShowPageBinding;
import com.squareup.picasso.Picasso;

public class IndividualPostShowPage extends AppCompatActivity {

    ActivityIndividualPostShowPageBinding binding;
    PostModel postData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIndividualPostShowPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        postData = (PostModel) getIntent().getSerializableExtra("PostData");
        if (postData != null) {
            loadData();
        }

    }

    private void loadData() {
        binding.mainName.setText(postData.getName());
        binding.mainDescription.setText(postData.getDescription());
        binding.mainPrice.setText(postData.getPrice());
        binding.mainLocation.setText(postData.getLocation());
        Picasso.get().load(postData.getPictureUri()).placeholder(null).into(binding.mainImage);
    }
}