package com.example.ptc_trial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ptc_trial.Models.User;
import com.example.ptc_trial.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private GoogleSignInClient mGoogleSignInClient;
    ActivitySignUpBinding binding;
    FirebaseAuth mAuth;
    private int RC_SIGN_IN = 1;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.gotoLogin.setOnClickListener(this);
        binding.signUpButton.setOnClickListener(this);
        binding.googleSignIn2.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        createRequest();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gotoLogin:
                Intent intent = new Intent(SignUp.this, LogIn.class);
                finish();
                startActivity(intent);
                break;
            case R.id.signUpButton:
                validate();
                break;
            case R.id.googleSignIn2:
                signIn();
        }
    }

    private void validate() {
        if (binding.userName.getText().toString().isEmpty()) {
            binding.userName.setError("Please enter First Name");
        } else if (binding.email.getText().toString().isEmpty()) {
            binding.email.setError("Please enter email");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
            binding.email.setError("Please enter valid email");
        } else if (binding.password.getText().toString().isEmpty()) {
            binding.password.setError("Please provide password");
        } else if (binding.password.getText().length() < 6) {
            binding.password.setError("Please provide password up-to 6 characters");
        } else if (binding.phone.getText().toString().isEmpty()) {
            binding.phone.setError("Please provide Phone Number");
        } else {

//            Intent intent=new Intent(this,PhoneVerificationScreen.class);
//            User user = new User(binding.userName.getText().toString(),binding.email.getText().toString(),binding.password.getText().toString(),
//                    "+92"+binding.phone.getText().toString());
//            intent.putExtra("User",user);
//            this.finish();
//            startActivity(intent);
            binding.progressBar2.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(binding.email.getText().toString(), binding.password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task1) {
                                if (task1.isSuccessful()) {
                                    User user = new User(binding.userName.getText().toString(), binding.email.getText().toString(),
                                            binding.password.getText().toString(), binding.phone.getText().toString());
                                    String id = task.getResult().getUser().getUid();
                                    database.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Boolean flag = false;
                                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                User user1 = snapshot1.getValue(User.class);
                                                if (user1.getEmail().equals(user.getEmail())) {
                                                    Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
                                                    flag = true;
                                                }
                                            }
                                            if (!flag) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Registered Successfully, Please login",
                                                        Toast.LENGTH_SHORT).show();
                                                database.getReference().child("Users").child(id).setValue(user);
                                            }
                                            binding.progressBar2.setVisibility(View.INVISIBLE);
                                            Intent intent = new Intent(getApplicationContext(), LogIn.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        binding.progressBar2.setVisibility(View.INVISIBLE);
                        binding.password.setText("");
                        binding.userName.setText("");
                        binding.email.setText("");
                        binding.phone.setText("");
                    }
                }
            });
        }
    }

    private void createRequest() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        binding.progressBar2.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.progressBar2.setVisibility(View.INVISIBLE);
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String id = firebaseUser.getUid();
                            User user = new User();
                            user.setEmail(firebaseUser.getEmail());
                            user.setUserName(firebaseUser.getDisplayName());
                            user.setProfilePic(firebaseUser.getPhotoUrl().toString());
                            database.getReference().child("Users").child(id).setValue(user);
                            Toast.makeText(getApplicationContext(), "Signed in with Google", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Unsuccessful, Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}