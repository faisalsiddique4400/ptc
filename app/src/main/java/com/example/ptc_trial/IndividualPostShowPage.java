package com.example.ptc_trial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

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
        binding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "03123456789"));
                startActivity(intent);
            }
        });

        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", "0312345678");
                smsIntent.putExtra("sms_body", "Hi there, \n I saw your post regarding the selling of the pet. May I know if it is available?");
                startActivity(smsIntent);
            }
        });

    }

    private void loadData() {
        binding.mainName.setText(postData.getName());
        binding.mainDescription.setText(postData.getDescription());
        binding.mainPrice.setText(postData.getPrice());
        binding.mainLocation.setText(postData.getLocation());
        Picasso.get().load(postData.getPictureUri()).placeholder(null).into(binding.mainImage);
    }
}