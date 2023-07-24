package com.example.mywhatsapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mywhatsapp.Models.Users;
import com.example.mywhatsapp.databinding.ActivitySettingsBinding;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Settings extends AppCompatActivity {

    ActivitySettingsBinding binding;

    String imageurl = "";
    FirebaseAuth auth;
    FirebaseStorage storage;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        // uploading by deafault images



        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, MainActivity.class));
            }
        });


        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 35);
            }
        });


        database.getReference().child("users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                binding.updatename.setText(users.getUsername());
                binding.status.setText(users.getStatus());
                Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.dplogo).into(binding.profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatename=binding.updatename.getText().toString();
                String status=binding.status.getText().toString();
                HashMap<String, Object> updateobj=new HashMap<>();
                updateobj.put("username",updatename);
                updateobj.put("status",status);
                database.getReference().child("users").child(auth.getUid()).updateChildren(updateobj);
                Toast.makeText(Settings.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }


        @Override
        protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 35 && data.getData() != null) {
                Uri urls = data.getData();
                Picasso.get().load(urls).into(binding.profileImage);


                final StorageReference reference = storage.getReference().child("Profiles Pictures")
                        .child(auth.getUid());
                reference.putFile(urls).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference().child("users").child(auth.getUid()).child("profilepic").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    }
