package com.example.ptc_trial;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ptc_trial.Chat.Chat;
import com.example.ptc_trial.PostScreen.Post;
import com.example.ptc_trial.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return loadFragment(item.getItemId());
            }
        });
    }


    boolean loadFragment(int fragmentId) {
        switch (fragmentId) {
            case R.id.Home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Home()).commit();
                return true;
            case R.id.Chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Chat()).commit();
                return true;
            case R.id.Post:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Post()).commit();
                return true;
            case R.id.Store:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Store()).commit();
                return true;
            case R.id.Settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Settings()).commit();
                return true;
            default:
                return false;

        }

    }

    @Override
    public void onBackPressed() {

        if (binding.bottomNavigation.getSelectedItemId() == R.id.Home) {
            super.onBackPressed();
            finish();
        } else {
            binding.bottomNavigation.setSelectedItemId(R.id.Home);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_out, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                mAuth.signOut();
                finish();
                return true;
        }
        return false;
    }
}