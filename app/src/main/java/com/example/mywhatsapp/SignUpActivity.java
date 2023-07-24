package com.example.mywhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.mywhatsapp.Models.Users;
import com.example.mywhatsapp.databinding.ActivitySignUpBinding;
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

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    ProgressDialog progress;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;
    int RC_SIGN_IN = 65;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        getSupportActionBar().hide();
        progress = new ProgressDialog(this);
        progress.setTitle("Create Account");
        progress.setMessage("we are creating your account");

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.etusername.getText().toString().isEmpty()) {
                    binding.etusername.setError("Enter your name");
                    return;
                }
                if (binding.etemail.getText().toString().isEmpty()) {
                    binding.etemail.setError("Enter your email");
                    return;
                }
                if (binding.etpassword.getText().toString().isEmpty()) {
                    binding.etpassword.setError("Enter your password");
                    return;
                }
                progress.show();
                auth.createUserWithEmailAndPassword
                        (binding.etemail.getText().toString(), binding.etpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress.dismiss();
                        if (task.isSuccessful()) {
                            Users user = new Users(binding.etusername.getText().toString(), binding.etemail.getText().toString(), binding.etpassword.getText().toString());
                            String id = task.getResult().getUser().getUid();
                            user.setUserId(id);
                            database.getReference().child("users").child(id).setValue(user);
                            Toast.makeText(SignUpActivity.this, "successfull signup ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            //  intent.putExtra("email_key",user.getMail());
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(SignUpActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }

    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        this.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signinActivity(View view) {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Tag", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Tag", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(SignUpActivity.this, "succesfull", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            Users users = new Users();
                            users.setMail(user.getEmail());
                            users.setUserId(user.getUid());
                            users.setUsername(user.getDisplayName());
                            users.setProfilepic(user.getPhotoUrl().toString());
                            database.getReference().child("users").child(user.getUid()).setValue(users);
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            // intent.putExtra("email_key",user.getEmail());
                            startActivity(intent);
                            finish();

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //  updateUI(null);
                        }
                    }
                });

    }


}