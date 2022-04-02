package com.example.ptc_trial;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ptc_trial.Models.User;
import com.example.ptc_trial.databinding.ActivityLogInBinding;
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
import com.google.firebase.database.FirebaseDatabase;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    ActivityLogInBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkConnection();
        binding.gotoSignUp.setOnClickListener(this);
        binding.signInButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        createRequest();
        binding.googleSignIn.setOnClickListener(this);

    }

    private void checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        if (!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(LogIn.this);
            builder.setMessage("Please turn on your internet connection");
            builder.setTitle("Internet not Connected");
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            binding.email.setEnabled(false);
            binding.password.setEnabled(false);
            binding.signInButton.setEnabled(false);
            binding.gotoSignUp.setEnabled(false);
            binding.googleSignIn.setEnabled(false);
            binding.facebook.setEnabled(false);
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        }
//    }

    private void createRequest() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gotoSignUp:
                Intent intent = new Intent(LogIn.this, SignUp.class);
                finish();
                startActivity(new Intent(LogIn.this, SignUp.class));
                break;
            case R.id.signInButton:
                validate();
                break;
            case R.id.googleSignIn:
                signIn();
                break;
        }
    }

    private void validate() {
        if (binding.email.getText().toString().isEmpty()) {
            binding.email.setError("Please enter email");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
            binding.email.setError("Please enter valid email");
        } else if (binding.password.getText().toString().isEmpty()) {
            binding.password.setError("Please provide password");
        } else if (binding.password.getText().length() < 6) {
            binding.password.setError("Please provide password up-to 6 characters");
        } else {

            binding.progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mAuth.signInWithEmailAndPassword(binding.email.getText().toString(), binding.password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        if (mAuth.getCurrentUser().isEmailVerified()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            binding.progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Please Verify email", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Sorry Couldn't Sign In", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
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
        binding.progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.progressBar.setVisibility(View.INVISIBLE);
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String id = firebaseUser.getUid();
                            User user = new User(firebaseUser.getDisplayName(), firebaseUser.getEmail());
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
